package co.istad.ifinder.features.user.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(

        String uuid,

        String firstName,

        String lastName,

        String email,

        LocalDate dob,

        String phoneNumber,

        String profileImage,

        String country,

        String city,

        boolean isBlock,

        boolean isDelete,

        LocalDateTime createdAt,

        LocalDateTime lastModifiedAt
) {
}
