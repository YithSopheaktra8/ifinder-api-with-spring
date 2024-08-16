package co.istad.ifinder.features.bookmarks.dto;

public record BookmarkResponse(

        String uuid,

        String title,

        String url,

        String icon,

        String folderUuid,

        String folderName,

        String userUuid
) {
}
