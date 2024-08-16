package co.istad.ifinder.features.folder.dto;

import jakarta.validation.constraints.NotBlank;

public record FolderUpdateRequest(

        @NotBlank(message = "Folder name is required")
        String folderName,

        @NotBlank(message = "Folder description is required")
        String userUuid,

        @NotBlank(message = "Folder description is required")
        String folderUuid
) {
}
