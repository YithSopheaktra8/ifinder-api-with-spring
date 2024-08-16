package co.istad.ifinder.features.media;

import co.istad.ifinder.features.media.dto.MediaResponse;
import co.istad.ifinder.utils.MediaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaServiceImpl implements MediaService {

    @Value("${media.server-path}")
    private String server_path;

    @Value("${media.base-uri}")
    private String baseUri;


    /**
     * @param file
     * @param folderName
     * @return MediaResponse
     * @throws ResponseStatusException
     * @since 20/May/2024
     * This method uploads a single file to the server
     */
    @Override
    public MediaResponse uploadSingle(MultipartFile file, String folderName) {

        String newName = UUID.randomUUID().toString();

        String extension = MediaUtil.extractExtension(Objects.requireNonNull(file.getOriginalFilename()));

        newName = newName + "." + extension;

        Path path = Paths.get(server_path + folderName + "/" + newName);

        log.info("Uploading file to: " + path);

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getLocalizedMessage()
            );
        }

        return MediaResponse.builder()
                .name(newName)
                .contentType(file.getContentType())
                .extension(extension)
                .size(file.getSize())
                .uri(String.format("%s%s/%s", baseUri, folderName, newName))
                .build();
    }
}
