package co.istad.ifinder.features.bookmarks.dto;

public record BookmarkDeleteRequest(
        String bookmarkUuid,
        String userUuid
) {
}
