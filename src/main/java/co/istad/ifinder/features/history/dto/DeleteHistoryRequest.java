package co.istad.ifinder.features.history.dto;

import jakarta.validation.constraints.NotBlank;

public record DeleteHistoryRequest(
        @NotBlank(message = "User uuid is required")
        String userUuid,

        @NotBlank(message = "History uuid is required")
        String historyUuid
) {
}
