package co.istad.ifinder.features.media;

import co.istad.ifinder.features.media.dto.MediaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/media")
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/upload-image")
    @ResponseStatus(HttpStatus.CREATED)
    MediaResponse uploadSingle(@RequestPart MultipartFile file) {

        return mediaService.uploadSingle(file,"IMAGE");
    }

}
