package co.istad.ifinder.features.auth;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.Folder;
import co.istad.ifinder.domain.Role;
import co.istad.ifinder.domain.Token;
import co.istad.ifinder.domain.User;
import co.istad.ifinder.features.auth.dto.*;
import co.istad.ifinder.features.collection.CollectionRepository;
import co.istad.ifinder.features.feedback.FeedbackRepository;
import co.istad.ifinder.features.folder.FolderRepository;
import co.istad.ifinder.features.token.AuthTokenService;
import co.istad.ifinder.features.token.TokenRepository;
import co.istad.ifinder.features.user.RoleRepository;
import co.istad.ifinder.features.user.UserRepository;
import co.istad.ifinder.mapper.AuthMapper;
import co.istad.ifinder.mapper.TotalRecordMapper;
import co.istad.ifinder.utils.GenerateNumberUtil;
import co.istad.ifinder.utils.PasswordValidator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final RoleRepository roleRepository;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final AuthTokenService authTokenService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final TokenRepository tokenRepository;
    private final TemplateEngine templateEngine;
    private final CollectionRepository collectionRepository;
    private final FeedbackRepository feedbackRepository;
    private final TotalRecordMapper totalRecordMapper;
    private final FolderRepository folderRepository;


    @Value("${spring.mail.username}")
    private String adminMail;

    /**
     * @param registerRequest is dto that contain the information of user when register account.
     * @author Yith Sopheaktra
     * @since 20/May/2024
     * register method is for user register
     */
    @Transactional
    @Override
    public BaseMessage register(RegisterRequest registerRequest) throws MessagingException {

        String verifyCode = GenerateNumberUtil.generateCodeNumber();

        User user = authMapper.mapFromRegisterCreateRequest(registerRequest);
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        if (!registerRequest.password().equals(registerRequest.confirmPassword())) {
            return BaseMessage.builder().message("Password and Confirm Password must be same").build();
        }


        if (!PasswordValidator.validate(registerRequest.password())) {
            return BaseMessage.builder().message("Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character").build();
        }

        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setUuid(UUID.randomUUID().toString());
        user.setIsDelete(false);
        user.setIsVerified(false);
        user.setIsBlock(false);
        user.setVerificationCode(verifyCode);
        // set default role USER when create user
        List<Role> roleList = new ArrayList<>();
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User role does not exist!"
                ));
        roleList.add(role);
        user.setRoles(roleList);

        Folder folder = new Folder();
        folder.setName("Bookmark");
        folder.setUuid(UUID.randomUUID().toString());
        folder.setUser(user);
        folderRepository.save(folder);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);


        Context context = new Context();
        context.setVariable("verifyCode", user.getVerificationCode());
        String htmlTemplate = templateEngine.process("auth/verify-mail", context);
        helper.setTo(registerRequest.email());
        helper.setFrom(adminMail);
        helper.setSubject("IFinder verify code");
        helper.setText(htmlTemplate, true);
        javaMailSender.send(message);
        userRepository.save(user);

        return BaseMessage.builder().message("Verify code has been send to your email. Please check your email.").build();
    }


    /**
     * @param verifyCodeRequest is dto that has user code and user email.
     * @author Yith Sopheaktra
     * @since 20/May/2024
     * verifyUserAccount is for verify user account with the verification code that has been sent to user email.
     */
    @Override
    public BaseMessage verifyUserAccount(VerifyCodeRequest verifyCodeRequest) {

        User user = authRepository.findByEmailAndVerificationCodeAndIsDeleteFalse(verifyCodeRequest.email(), verifyCodeRequest.verificationCode())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found!"
                ));

        if (user.getIsVerified().equals(true)) {
            return BaseMessage.builder().message("Your account has already been verified.").build();
        }

        user.setIsVerified(true);
        userRepository.save(user);

        return BaseMessage.builder().message("Your account has been verified.").build();
    }

    /**
     * @param loginRequest is dto that contain the information of user when login.
     * @author Yith Sopheaktra
     * @since 20/May/2024
     * login is for user login to the system. It will return the token to user.
     */
    @Override
    public AuthResponse userLogin(LoginRequest loginRequest) {

        if (!userRepository.existsByEmail(loginRequest.email())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User has not been found!"
            );
        }


        User user = userRepository.findByEmailAndIsBlockFalseAndIsDeleteFalse(loginRequest.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found!"
                ));

        if (!user.getIsVerified()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Your account has not been verified."
            );
        }

        if(user.getRoles().stream().noneMatch(role -> role.getName().equals("USER"))){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You are not authorized to access this resource."
            );
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
        );

        Authentication authentication = daoAuthenticationProvider.authenticate(auth);

        return authTokenService.createToken(authentication);
    }

    @Override
    public AuthResponse adminLogin(LoginRequest loginRequest) {


        if (!userRepository.existsByEmail(loginRequest.email())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User has not been found!"
            );
        }


        User user = userRepository.findByEmailAndIsBlockFalseAndIsDeleteFalse(loginRequest.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found!"
                ));

        if (!user.getIsVerified()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Your account has not been verified."
            );
        }

        if(user.getRoles().stream().noneMatch(role -> role.getName().equals("ADMIN"))){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You are not authorized to access this resource."
            );
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
        );

        Authentication authentication = daoAuthenticationProvider.authenticate(auth);

        return authTokenService.createToken(authentication);
    }

    /**
     * @param refreshTokenRequest is dto that contain the refresh token.
     * @return RefreshTokenResponse that contain the new token.
     * @author Yith Sopheaktra
     * @since 20/May/2024
     * refreshToken is for refresh token when the token is expired.
     */
    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        return authTokenService.refreshToken(refreshTokenRequest);
    }

    @Override
    public BaseMessage resendVerificationCode(String email) throws MessagingException {


        String verifyCode = GenerateNumberUtil.generateCodeNumber();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found!"
                ));

        if (user.getIsVerified()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Your account has already been verified."
            );
        }

        user.setVerificationCode(verifyCode);
        userRepository.save(user);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        Context context = new Context();
        context.setVariable("verifyCode", user.getVerificationCode());
        String htmlTemplate = templateEngine.process("auth/verify-mail", context);
        helper.setTo(email);
        helper.setFrom(adminMail);
        helper.setSubject("IFinder verify code");
        helper.setText(htmlTemplate, true);
        javaMailSender.send(message);

        return BaseMessage.builder().message("Verify code has been send to your email. Please check your email.").build();
    }


    /**
     * @param changePasswordRequest is dto that contain the old password, new password and confirm password.
     * @param jwt                   is the token that user has when login to the system.
     * @return BaseMessage that contain the message of the result of changing password.
     * @since 20/May/2024
     * changePassword is for user change password. It will return the message to user.
     */
    @Override
    public BaseMessage changePassword(ChangePasswordRequest changePasswordRequest, Jwt jwt) {

        User user = userRepository.findByEmail(jwt.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found!"
                ));

        if (!passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())) {
            return BaseMessage.builder().message("Old password is incorrect").build();
        }

        if (!changePasswordRequest.newPassword().equals(changePasswordRequest.confirmPassword())) {
            return BaseMessage.builder().message("New password and Confirm password must be same").build();
        }

        if (!PasswordValidator.validate(changePasswordRequest.newPassword())) {
            return BaseMessage.builder().message("Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character").build();
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword()));
        userRepository.save(user);

        return BaseMessage.builder().message("Password has been changed").build();

    }

    /**
     * @param email is the email that user want to send the verify code.
     * @return BaseMessage that contain the message of the result of sending verify code.
     * @since 20/May/2024
     * sendVerifyCode is for send verify code to user email. It will return the message to user.
     */
    @Override
    public BaseMessage sendVerifyCode(String email) throws MessagingException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found!"
                ));

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String verifyCode = GenerateNumberUtil.generateCodeNumber();

        Token token = new Token();
        token.setUser(user);
        token.setVerifyCode(verifyCode);
        tokenRepository.save(token);

        Context context = new Context();
        context.setVariable("verifyCode", verifyCode);
        String htmlTemplate = templateEngine.process("auth/verify-mail", context);
        helper.setTo(email);
        helper.setFrom(adminMail);
        helper.setSubject("IFinder verify code");
        helper.setText(htmlTemplate, true);

        javaMailSender.send(message);
        return BaseMessage.builder()
                .message("Verify code has been send to your email. Please check your email.")
                .build();
    }

    /**
     * @param resetTokenRequest is dto that contain the email and verify code.
     * @return ResetTokenResponse that contain the reset token, email and verify code.
     * @since 20/May/2024
     * getResetToken is for get reset token. It will return the reset token to user.
     */
    @Override
    public ResetTokenResponse getResetToken(ResetTokenRequest resetTokenRequest) {

        User user = userRepository.findByEmail(resetTokenRequest.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found!"
                ));

        Token token = tokenRepository.findByUser(user);
        if (!token.getVerifyCode().equals(resetTokenRequest.verifyCode())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid verify code"
            );
        }

        return ResetTokenResponse.builder()
                .resetToken(authTokenService.generateResetToken(user.getEmail(), user, token))
                .email(user.getEmail())
                .verifyCode(token.getVerifyCode())
                .message("Reset token has been generated")
                .build();

    }

    /**
     * @param resetPasswordRequest is dto that contain the reset token, new password and confirm password.
     * @return BaseMessage that contain the message of the result of reset password.
     * @since 20/May/2024
     * resetPassword is for reset password. It will return the message to user.
     */
    @Override
    public BaseMessage resetPassword(ResetPasswordRequest resetPasswordRequest) {

        if (!resetPasswordRequest.newPassword().equals(resetPasswordRequest.confirmPassword())) {
            return BaseMessage.builder().message("New password and Confirm password must be same").build();
        }

        return authTokenService.resetPassword(resetPasswordRequest.resetToken(), resetPasswordRequest.newPassword());

    }

    @Override
    public TotalRecordResponse getTotalRecord() {

        Long totalCollection = collectionRepository.count();

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Role not found"
                ));

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Role not found"
                ));

        List<User> users = userRepository.findAllByRolesAndIsBlockFalseAndIsDeleteFalse(List.of(userRole));
        Long totalUser = (long) users.size();

        List<User> admins = userRepository.findAllByRolesAndIsBlockFalseAndIsDeleteFalse(List.of(adminRole));
        Long totalAdmins = (long) admins.size();

        Long totalFeedback = feedbackRepository.count();

        return totalRecordMapper.mapToTotalRecordResponse(totalUser, totalAdmins, totalFeedback, totalCollection);
    }
}
