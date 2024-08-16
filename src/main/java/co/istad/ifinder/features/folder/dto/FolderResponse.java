package co.istad.ifinder.features.folder.dto;

import co.istad.ifinder.domain.Bookmark;
import co.istad.ifinder.features.bookmarks.dto.BookmarkResponse;

import java.util.List;

public record FolderResponse(

        String uuid,

        String name,

        List<BookmarkResponse> bookmarks,

        String createdAt,

        String lastModifiedAt
) {
}
