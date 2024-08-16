package co.istad.ifinder.features.history.dto;

import jakarta.validation.constraints.NotBlank;

public record AddHistoryRequest(

        @NotBlank(message = "userUuid is required")
        String userUuid,

        @NotBlank(message = "url is required")
        String url
) {
}
