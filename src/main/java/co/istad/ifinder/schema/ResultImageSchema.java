package co.istad.ifinder.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultImageSchema {
    private int width;

    private int height;

    private int byteSize;

    private String contextLink;

    private String thumbnailLink;

    private int thumbnailWidth;

    private int thumbnailHeight;
}
