package co.istad.ifinder.features.bookmarks;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.Bookmark;
import co.istad.ifinder.domain.Collection;
import co.istad.ifinder.domain.Folder;
import co.istad.ifinder.domain.User;
import co.istad.ifinder.features.bookmarks.dto.BookmarkCreateRequest;
import co.istad.ifinder.features.bookmarks.dto.BookmarkDeleteRequest;
import co.istad.ifinder.features.bookmarks.dto.BookmarkResponse;
import co.istad.ifinder.features.bookmarks.dto.BookmarkUpdateRequest;
import co.istad.ifinder.features.collection.CollectionRepository;
import co.istad.ifinder.features.folder.FolderRepository;
import co.istad.ifinder.features.user.UserRepository;
import co.istad.ifinder.mapper.BookmarkMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkServiceImpl implements BookmarkService {

    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkMapper bookmarkMapper;

    /**
     * @param bookmarkCreateRequest
     * @return BaseMessage
     * @throws ResponseStatusException
     * @since 20/May/2024
     * This method creates a bookmark for a user
     */
    @Override
    public BookmarkResponse createBookmark(BookmarkCreateRequest bookmarkCreateRequest, Jwt jwt) {

        String email = jwt.getClaim("iss");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        if(!bookmarkCreateRequest.userUuid().equals(user.getUuid())){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to create bookmark on this user account"
            );
        }

        user.getFolders().forEach((e)->{
            if(e.getUuid().equals(bookmarkCreateRequest.folderUuid())){
                e.getBookmarks().stream().forEach((element)->{
                    if(element.getTitle().equals(bookmarkCreateRequest.title())){
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "User bookmark is already existed! in this folder"
                        );
                    }

                });
            }

        });

        Folder folder = new Folder();

        if (folderRepository.existsByUuid(bookmarkCreateRequest.folderUuid())) {
            folder = folderRepository.findByUuid(bookmarkCreateRequest.folderUuid())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Folder not found"
                    ));
            if (!folder.getUser().getUuid().equals(user.getUuid())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Folder does not belong to user"
                );
            }

        }

        Bookmark bookmark = new Bookmark();
        bookmark.setUrl(bookmarkCreateRequest.url());
        if (folder.getUuid() != null) {
            bookmark.setFolders(folder);
        }
        bookmark.setUser(user);
        bookmark.setTitle(bookmarkCreateRequest.title());
        bookmark.setIcon(bookmarkCreateRequest.icon());
        bookmark.setUuid(UUID.randomUUID().toString());

        Bookmark bookmark1 = bookmarkRepository.save(bookmark);

        return bookmarkMapper.mapFromBookmarkToBookmarkResponse(bookmark1);
    }

    /**
     * @param userUuid
     * @param page
     * @param size
     * @return Page<BookmarkResponse>
     * @throws ResponseStatusException
     * @since 20/May/2024
     * This method returns all bookmarks for a user
     */
    @Override
    public Page<BookmarkResponse> findAllBookmarks(String userUuid, int page, int size) {

        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user, pageRequest);

        return bookmarks.map(bookmarkMapper::mapFromBookmarkToBookmarkResponse);
    }

    /**
     * @param bookmarkDeleteRequest
     * @return BaseMessage
     * @throws ResponseStatusException
     * @since 20/May/2024
     * This method deletes a bookmark
     */
    @Override
    public BaseMessage deleteBookmark(BookmarkDeleteRequest bookmarkDeleteRequest, Jwt jwt) {


        String email = jwt.getClaim("iss");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Bookmark bookmark = bookmarkRepository.findByUuid(bookmarkDeleteRequest.bookmarkUuid())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Bookmark not found"
                ));

        if(!user.getUuid().equals(bookmarkDeleteRequest.userUuid())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You don't have any authorized to delete this bookmark"
            );
        }

        bookmarkRepository.delete(bookmark);

        return BaseMessage.builder()
                .message("Bookmark deleted successfully")
                .build();
    }

    /**
     * @param bookmarkUpdateRequest
     * @return BaseMessage
     * @throws ResponseStatusException
     * @since 20/May/2024
     * This method updates the title of a bookmark
     */
    @Override
    public BaseMessage updateBookmarkTitle(BookmarkUpdateRequest bookmarkUpdateRequest, Jwt jwt) {

        String email = jwt.getClaim("iss");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Bookmark bookmark = bookmarkRepository.findByUuid(bookmarkUpdateRequest.bookmarkUuid())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Bookmark not found"
                ));

        if(!bookmark.getUser().getUuid().equals(user.getUuid())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You don't have any authorized to update this bookmark title"
            );
        }

        bookmark.setTitle(bookmarkUpdateRequest.title());
        bookmarkRepository.save(bookmark);

        return BaseMessage.builder()
                .message("Bookmark title updated successfully")
                .build();
    }

    @Override
    public BookmarkResponse findBookmarkByUuid(String bookmarkId) {

        Bookmark bookmark = bookmarkRepository.findByUuid(bookmarkId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Bookmark not found"
                ));

        return bookmarkMapper.mapFromBookmarkToBookmarkResponse(bookmark);
    }

}