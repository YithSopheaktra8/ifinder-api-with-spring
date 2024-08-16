package co.istad.ifinder.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Video {

    private String videoId;

    private String chanel;

    private String title;

    private String description;

    private String publishedAt;

    private String thumbnail;

    private String viewCount;

    private String duration;

    private String youtubeLink;
}
