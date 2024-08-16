package co.istad.ifinder.features.folder.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record FolderCreateRequest(

        @NotBlank(message = "userUuid is required")
        String userUuid,

        @NotBlank(message = "name is required")
        String name

        ) {
}
