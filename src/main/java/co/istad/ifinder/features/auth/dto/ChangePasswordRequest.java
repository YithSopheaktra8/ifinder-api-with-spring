package co.istad.ifinder.features.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(

        @NotBlank(message = "old password is required")
        String oldPassword,

        @NotBlank(message = "new password is required")
        String newPassword,

        @NotBlank(message = "confirm password is required")
        String confirmPassword
) {
}
