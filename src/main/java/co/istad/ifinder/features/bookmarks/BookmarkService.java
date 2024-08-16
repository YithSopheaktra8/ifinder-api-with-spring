package co.istad.ifinder.features.bookmarks;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.features.bookmarks.dto.BookmarkCreateRequest;
import co.istad.ifinder.features.bookmarks.dto.BookmarkDeleteRequest;
import co.istad.ifinder.features.bookmarks.dto.BookmarkResponse;
import co.istad.ifinder.features.bookmarks.dto.BookmarkUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

public interface BookmarkService {

    BookmarkResponse createBookmark(BookmarkCreateRequest bookmarkCreateRequest, Jwt jwt);

    Page<BookmarkResponse> findAllBookmarks(String userUuid, int page, int size);

    BaseMessage deleteBookmark(BookmarkDeleteRequest bookmarkDeleteRequest, Jwt jwt);

    BaseMessage updateBookmarkTitle(BookmarkUpdateRequest bookmarkUpdateRequest, Jwt jwt);

    BookmarkResponse findBookmarkByUuid(String bookmarkId);
}
