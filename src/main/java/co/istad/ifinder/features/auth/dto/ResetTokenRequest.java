package co.istad.ifinder.features.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetTokenRequest(

        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "verify code is required")
        String verifyCode
) {
}
