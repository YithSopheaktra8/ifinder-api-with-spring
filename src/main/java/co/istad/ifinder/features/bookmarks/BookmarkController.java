package co.istad.ifinder.features.bookmarks;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.features.bookmarks.dto.BookmarkCreateRequest;
import co.istad.ifinder.features.bookmarks.dto.BookmarkDeleteRequest;
import co.istad.ifinder.features.bookmarks.dto.BookmarkResponse;
import co.istad.ifinder.features.bookmarks.dto.BookmarkUpdateRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookmarkResponse createBookmark(@RequestBody @Valid BookmarkCreateRequest bookmarkCreateRequest,
                                           @AuthenticationPrincipal Jwt jwt) {

        return bookmarkService.createBookmark(bookmarkCreateRequest,jwt);
    }

    @GetMapping
    public Page<BookmarkResponse> findAllBookmarks(@RequestParam String userUuid,
                                                   @RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "5") int size) {

        return bookmarkService.findAllBookmarks(userUuid, page, size);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseMessage deleteBookmark(@RequestBody @Valid BookmarkDeleteRequest bookmarkDeleteRequest,
                                      @AuthenticationPrincipal Jwt jwt) {

        return bookmarkService.deleteBookmark(bookmarkDeleteRequest, jwt);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public BaseMessage updateBookmarkTitle(@RequestBody @Valid BookmarkUpdateRequest bookmarkUpdateRequest,
                                           @AuthenticationPrincipal Jwt jwt) {

        return bookmarkService.updateBookmarkTitle(bookmarkUpdateRequest, jwt);
    }

    @GetMapping("/{uuid}")
    public BookmarkResponse findBookmarkByUuid(@PathVariable String uuid) {
        return bookmarkService.findBookmarkByUuid(uuid);
    }

}
