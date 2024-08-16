package co.istad.ifinder.features.auth;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.features.auth.dto.*;
import jakarta.mail.MessagingException;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthService {

    BaseMessage register(RegisterRequest registerRequest) throws MessagingException;

    BaseMessage verifyUserAccount(VerifyCodeRequest verifyCodeRequest);

    AuthResponse userLogin(LoginRequest loginRequest);

    AuthResponse adminLogin(LoginRequest loginRequest);

    BaseMessage resendVerificationCode(String email) throws MessagingException;

    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    BaseMessage changePassword(ChangePasswordRequest changePasswordRequest, Jwt jwt);

    BaseMessage sendVerifyCode(String email) throws MessagingException;

    ResetTokenResponse getResetToken(ResetTokenRequest resetTokenRequest);

    BaseMessage resetPassword(ResetPasswordRequest resetPasswordRequest);

    TotalRecordResponse getTotalRecord();
}
