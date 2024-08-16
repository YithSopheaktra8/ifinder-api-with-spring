package co.istad.ifinder.features.video;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.Video;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface VideoService {

    Page<Video> searchVideo(int page, int size, String search) throws IOException;


}
