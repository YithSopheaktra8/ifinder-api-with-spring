package co.istad.ifinder.features.folder;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.base.BaseResponse;
import co.istad.ifinder.features.folder.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FolderService {

    BaseMessage createFolder(FolderCreateRequest folderCreateRequest, Jwt jwt);

    BaseMessage deleteFolder(FolderDeleteRequest folderDeleteRequest, Jwt jwt);

    FolderResponse findFolder(String folderUuid);

    Page<FolderResponse> findAllFolders(String userUuid, int page, int size);

    FolderResponse updateFolder(FolderUpdateRequest folderUpdateRequest);

    File exportFolderData(String userUuid, DownloadFolderRequest downloadFolderRequest) throws IOException;

    BaseResponse<?> importFolderData(MultipartFile file, String userUuid) throws IOException;

}
