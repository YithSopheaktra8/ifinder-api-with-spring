package co.istad.ifinder.features.bookmarks.dto;

import jakarta.validation.constraints.NotBlank;

public record BookmarkCreateRequest(

        @NotBlank(message = "User UUID is required")
        String userUuid,

        String folderUuid,

        @NotBlank(message = "title is required")
        String title,

        @NotBlank(message = "url is required")
        String url,

        @NotBlank(message = "icon is required")
        String icon

) {
}
