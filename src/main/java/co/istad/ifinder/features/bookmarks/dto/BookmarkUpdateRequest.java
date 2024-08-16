package co.istad.ifinder.features.bookmarks.dto;

import jakarta.validation.constraints.NotBlank;

public record BookmarkUpdateRequest(

        @NotBlank(message = "Bookmark UUID is required")
        String bookmarkUuid,

        String title,

        @NotBlank(message = "User UUID is required")
        String userUuid
) {
}
