package co.istad.ifinder.features.user.dto;

import jakarta.validation.constraints.Email;

import java.time.LocalDate;

public record UserUpdateRequest(

        String firstName,

        String lastName,

        @Email(message = "Email is invalid", regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
        String email,

        String username,

        LocalDate dob,

        String profileImage,

        String phoneNumber,

        String country,

        String city

) {
}
