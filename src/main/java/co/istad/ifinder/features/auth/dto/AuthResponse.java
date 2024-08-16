package co.istad.ifinder.features.auth.dto;

import co.istad.ifinder.features.user.dto.UserResponse;

public record AuthResponse(
        String type,
        String accessToken,
        String refreshToken,
        UserResponse user
) {
}
