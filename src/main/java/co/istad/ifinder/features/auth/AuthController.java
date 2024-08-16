package co.istad.ifinder.features.auth;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.features.auth.dto.*;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseMessage register(@Valid @RequestBody RegisterRequest registerRequest) throws MessagingException {

        return authService.register(registerRequest);
    }

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    public BaseMessage verifyAccount(@Valid @RequestBody VerifyCodeRequest verifyCodeRequest) {

        return authService.verifyUserAccount(verifyCodeRequest);
    }

    @PostMapping("/{email}/resend-verification-code")
    @ResponseStatus(HttpStatus.OK)
    public BaseMessage resendVerificationCode(@PathVariable String email) throws MessagingException {

        return authService.resendVerificationCode(email);
    }

    @PostMapping("/user/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse userLogin(@Valid @RequestBody LoginRequest loginRequest) {

        return authService.userLogin(loginRequest);
    }
    @PostMapping("/admin/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse adminLogin(@Valid @RequestBody LoginRequest loginRequest) {

        return authService.adminLogin(loginRequest);
    }

    @PostMapping("/refresh-token")
    public AuthResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {

        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/change-password")
    public BaseMessage changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
                                      @AuthenticationPrincipal Jwt jwt) {

        return authService.changePassword(changePasswordRequest, jwt);
    }

    @PostMapping("/reset-token")
    public ResetTokenResponse getResetToken(@Valid @RequestBody ResetTokenRequest resetTokenRequest) {

        return authService.getResetToken(resetTokenRequest);
    }

    @PostMapping("/reset-password")
    public BaseMessage resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {

        return authService.resetPassword(resetPasswordRequest);
    }

    @PostMapping("/{email}/send-verify-code")
    public BaseMessage sendVerifyCode(@PathVariable String email) throws MessagingException {

        return authService.sendVerifyCode(email);
    }

    @GetMapping("/total-record")
    public TotalRecordResponse getTotalRecord() {

        return authService.getTotalRecord();
    }

}
