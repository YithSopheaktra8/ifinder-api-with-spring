package co.istad.ifinder.features.feedback.dto;

import jakarta.validation.constraints.NotBlank;

public record FeedbackRequest(

        @NotBlank(message = "User UUID is required")
        String userUuid,

        @NotBlank(message = "Feedback is required")
        String feedback
) {
}
