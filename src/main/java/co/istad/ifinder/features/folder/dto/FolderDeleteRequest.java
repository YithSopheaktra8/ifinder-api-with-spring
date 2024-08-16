package co.istad.ifinder.features.folder.dto;

import jakarta.validation.constraints.NotBlank;

public record FolderDeleteRequest(
        @NotBlank(message = "Folder uuid is required")
        String folderUuid,
        @NotBlank(message = "User uuid is required")
        String userUuid
) {
}
