package co.istad.ifinder.features.token;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.Token;
import co.istad.ifinder.domain.User;
import co.istad.ifinder.features.auth.dto.AuthResponse;
import co.istad.ifinder.features.auth.dto.RefreshTokenRequest;
import org.springframework.security.core.Authentication;

public interface AuthTokenService {

    AuthResponse createToken(Authentication authentication);

    String createAccessToken(Authentication authentication);

    String createRefreshToken(Authentication authentication);

    String generateResetToken(String email, User user, Token token);

    BaseMessage resetPassword(String token, String newPassword);

    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
