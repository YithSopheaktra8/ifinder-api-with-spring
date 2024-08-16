package co.istad.ifinder.features.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VerifyCodeRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Email is invalid", regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
        String email,

        @NotBlank(message = "verify code is required")
        String verificationCode
) {
}
