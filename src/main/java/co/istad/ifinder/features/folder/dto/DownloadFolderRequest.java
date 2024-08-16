package co.istad.ifinder.features.folder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DownloadFolderRequest(
        @NotNull(message = "Folder UUID is required")
        List<String> folderUuid
) {
}
