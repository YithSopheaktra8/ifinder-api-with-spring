package co.istad.ifinder.features.search.dto;

import jakarta.validation.constraints.NotBlank;

public record DataImportRequest(

        @NotBlank(message = "linkUri is required")
        String linkUri
) {
}
