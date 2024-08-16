package co.istad.ifinder.features.folder;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.base.BaseResponse;
import co.istad.ifinder.domain.Bookmark;
import co.istad.ifinder.domain.Folder;
import co.istad.ifinder.domain.User;
import co.istad.ifinder.features.bookmarks.BookmarkRepository;
import co.istad.ifinder.features.folder.dto.*;
import co.istad.ifinder.features.user.UserRepository;
import co.istad.ifinder.mapper.FolderMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final FolderMapper folderMapper;
    private final ObjectMapper objectMapper;
    private final BookmarkRepository bookmarkRepository;

    @Value("${json.file.directory}")
    private String EXPORT_DIR;


    /**
     * @param folderCreateRequest
     * @return BaseMessage
     * @throws ResponseStatusException
     * @since 20/May/2024
     * This method creates a folder for a user
     */
    @Override
    public BaseMessage createFolder(FolderCreateRequest folderCreateRequest, Jwt jwt) {

        User user = userRepository.findByUuid(folderCreateRequest.userUuid())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        String email = jwt.getClaimAsString("iss");
        if(!user.getEmail().equals(email)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed create folder on this user account"
            );
        }

        if (folderRepository.existsByNameAndUser(folderCreateRequest.name(),user)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Folder with name " + folderCreateRequest.name() + " already exists"
            );
        }

        Folder folder = new Folder();
        folder.setName(folderCreateRequest.name());
        folder.setUuid(UUID.randomUUID().toString());
        folder.setUser(user);
        folderRepository.save(folder);

        return BaseMessage.builder()
                .message("Folder created successfully")
                .build();
    }

    /**
     * @param folderDeleteRequest
     * @return BaseMessage
     * @throws ResponseStatusException
     * @since 20/May/2024
     * This method deletes a folder
     */
    @Override
    public BaseMessage deleteFolder(FolderDeleteRequest folderDeleteRequest, Jwt jwt) {

        Folder folder = folderRepository.findByUuid(folderDeleteRequest.folderUuid())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Folder not found"
                ));

        if(!folder.getUser().getUuid().equals(folderDeleteRequest.userUuid())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Folder does not belong to the user"
            );
        }

        String email = jwt.getClaimAsString("iss");
        if(!folder.getUser().getEmail().equals(email)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed delete folder on this user account"
            );
        }

        folderRepository.delete(folder);
        return BaseMessage.builder()
                .message("Folder deleted successfully")
                .build();
    }

    /**
     * @param folderUuid
     * @return FolderResponse
     * @throws ResponseStatusException
     * @since 20/May/2024
     * This method finds a folder
     */
    @Override
    public FolderResponse findFolder(String folderUuid) {

        Folder folder = folderRepository.findByUuid(folderUuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Folder not found"
                ));

        return folderMapper.mapFromFolderToFolderResponse(folder);
    }

    /**
     * @param userUuid
     * @param page
     * @param size
     * @return Page<FolderResponse>
     * @throws ResponseStatusException
     * @since 20/May/2024
     * This method finds all folders for a user
     */
    @Override
    public Page<FolderResponse> findAllFolders(String userUuid, int page, int size) {

        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Folder> folders = folderRepository.findAllByUser(user, pageRequest);

        return folders.map(folderMapper::mapFromFolderToFolderResponse);
    }


    /**
     * @param folderUpdateRequest
     * @return FolderResponse
     * @throws ResponseStatusException
     * @since 20/May/2024
     * This method updates a folder
     */
    @Override
    public FolderResponse updateFolder(FolderUpdateRequest folderUpdateRequest) {

        Folder folder = folderRepository.findByUuid(folderUpdateRequest.folderUuid())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Folder not found"
                ));

        if(!folder.getUser().getUuid().equals(folderUpdateRequest.userUuid())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Folder does not belong to the user"
            );
        }

        folder.setName(folderUpdateRequest.folderName());
        folderRepository.save(folder);

        return folderMapper.mapFromFolderToFolderResponse(folder);
    }

    /**
     * @param userUuid
     * @return File
     * @throws IOException
     * This method exports folder data
     */
    @Override
    public File exportFolderData(String userUuid, DownloadFolderRequest downloadFolderRequest) throws IOException {

        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        List<FolderResponse> folders = folderRepository.findAllByUser(user)
                .stream()
                .map(folderMapper::mapFromFolderToFolderResponse)
                .toList();

        List<FolderResponse> filteredFolders = folders.stream()
                .filter(folder -> downloadFolderRequest.folderUuid().contains(folder.uuid()))
                .toList();


        Path exportPath = Paths.get(EXPORT_DIR);
        if (!Files.exists(exportPath)) {
            Files.createDirectories(exportPath);
        }
        File jsonFile = exportPath.resolve("folders.json").toFile();
        try {
            objectMapper.writeValue(jsonFile, filteredFolders);
        } catch (Exception e) {
            log.error("Error exporting folder data", e);
        }
        return jsonFile;
    }

    /**
     * @param file
     * @param userUuid
     * @return BaseResponse<?>
     * @throws IOException
     * This method imports folder data
     */
    @Override
    public BaseResponse<?> importFolderData(MultipartFile file, String userUuid) throws IOException {

        if (file.isEmpty()) {
            throw new IOException("Uploaded file is empty");
        }

        List<FolderResponse> folders;
        try (var inputStream = file.getInputStream()) {
            folders = objectMapper.readValue(inputStream, new TypeReference<List<FolderResponse>>() {});
        }

        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));


        List<String> existingFolderNames = user.getFolders().stream()
                .map(Folder::getName)
                .toList();

        for (FolderResponse folder : folders) {

            Folder newFolder = new Folder();
            newFolder.setUuid(UUID.randomUUID().toString());

            String baseName = folder.name();
            String newName = baseName;
            int counter = 1;

            if (existingFolderNames.contains(baseName)) {
                if (existingFolderNames.stream().anyMatch(name -> name.matches(Pattern.quote(baseName) + " \\(imported\\)( \\d+)?"))) {
                    // Find the highest counter value used with "(imported)"
                    String regex = Pattern.quote(baseName) + " \\(imported\\)( \\d+)?";
                    Pattern pattern = Pattern.compile(regex);
                    for (String existingName : existingFolderNames) {
                        Matcher matcher = pattern.matcher(existingName);
                        if (matcher.find()) {
                            String number = matcher.group(1);
                            if (number != null) {
                                int num = Integer.parseInt(number.trim());
                                if (num >= counter) {
                                    counter = num + 1;
                                }
                            } else if (counter == 1) {
                                counter = 2; // If there's at least one without a number, start numbering from 2
                            }
                        }
                    }
                    newName = baseName + " (imported) " + counter;
                } else {
                    newName = baseName + " (imported)";
                }
                newFolder.setName(newName);
            }
             else {
                newFolder.setName(folder.name());
            }

            newFolder.setUser(user);
            Folder createdFolder = folderRepository.save(newFolder);

            List<Bookmark> bookmarks = new ArrayList<>();
            folder.bookmarks().forEach(bookmarkResponse -> {
                Bookmark bookmark = new Bookmark();
                bookmark.setTitle(bookmarkResponse.title());
                bookmark.setUrl(bookmarkResponse.url());
                bookmark.setIcon(bookmarkResponse.icon());
                bookmark.setUuid(UUID.randomUUID().toString());
                bookmark.setUser(createdFolder.getUser());
                bookmark.setFolders(createdFolder);
                bookmarks.add(bookmark);
            });
            bookmarkRepository.saveAll(bookmarks);

        }


        return BaseResponse.builder()
                .code(HttpStatus.CREATED.value())
                .description("Folders imported successfully")
                .build();

    }


}

