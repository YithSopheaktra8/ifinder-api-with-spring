package co.istad.ifinder.features.feedback.dto;

public record FeedbackResponse(

        String uuid,

        String profileImage,

        String firstName,

        String lastName,

        String feedback,

        String createdAt

) {
}
