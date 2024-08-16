package co.istad.ifinder.mapper;


import co.istad.ifinder.domain.Bookmark;
import co.istad.ifinder.features.bookmarks.dto.BookmarkResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")

public interface BookmarkMapper {

    @Mappings({
            @Mapping(source = "bookmark.folders.name", target = "folderName"),
            @Mapping(source = "bookmark.folders.uuid", target = "folderUuid"),
            @Mapping(source = "bookmark.user.uuid", target = "userUuid"),
    })
    BookmarkResponse mapFromBookmarkToBookmarkResponse(Bookmark bookmark);

}
