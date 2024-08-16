package co.istad.ifinder.mapper;

import co.istad.ifinder.domain.Folder;
import co.istad.ifinder.features.folder.dto.FolderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BookmarkMapper.class})
public interface FolderMapper {

    @Mapping(source = "bookmarks", target = "bookmarks")
    FolderResponse mapFromFolderToFolderResponse(Folder folder);

    @Mapping(source = "uuid", target = "user.uuid")
    Folder mapFromFolderResponseToFolder(FolderResponse folderResponse);

}
