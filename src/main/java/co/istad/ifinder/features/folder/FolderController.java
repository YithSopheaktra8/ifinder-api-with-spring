package co.istad.ifinder.features.folder;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.base.BaseResponse;
import co.istad.ifinder.features.folder.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/folders")
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public BaseMessage createFolder(@RequestBody @Valid FolderCreateRequest folderCreateRequest,
                                    @AuthenticationPrincipal Jwt jwt){

        return folderService.createFolder(folderCreateRequest, jwt);
    }

    @DeleteMapping
    public BaseMessage deleteFolder(@RequestBody @Valid FolderDeleteRequest folderDeleteRequest,
                                    @AuthenticationPrincipal Jwt jwt){

        return folderService.deleteFolder(folderDeleteRequest, jwt);
    }


    @GetMapping("/{uuid}")
    public FolderResponse findFolder(@PathVariable String uuid){

        return folderService.findFolder(uuid);
    }

    @GetMapping
    public Page<FolderResponse> findAllFolders(@RequestParam(defaultValue = "5" , required = false) int page,
                                               @RequestParam(defaultValue = "0", required = false) int size,
                                               @RequestParam String userUuid){

        return folderService.findAllFolders(userUuid,page, size);
    }

    @PutMapping
    public FolderResponse updateFolder(@RequestBody @Valid FolderUpdateRequest folderUpdateRequest){

        return folderService.updateFolder(folderUpdateRequest);
    }

    @PostMapping("/download")
    public ResponseEntity<FileSystemResource> downloadFoldersAsJson(@RequestParam String userUuid,
                                                                    @RequestBody @Valid DownloadFolderRequest downloadFolderRequest) throws IOException {
        File jsonFile = folderService.exportFolderData(userUuid, downloadFolderRequest);
        FileSystemResource fileResource = new FileSystemResource(jsonFile);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=folders.json");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(fileResource);
    }

    @PostMapping("/upload/{userUuid}")
    public BaseResponse<?> uploadFoldersJson(@RequestPart MultipartFile file,
                                             @PathVariable String userUuid) {
        try {
           return folderService.importFolderData(file, userUuid);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
