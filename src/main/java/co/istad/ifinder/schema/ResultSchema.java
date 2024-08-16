package co.istad.ifinder.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultSchema {

    private String kind;

    private String link;

    private String mime;

    private String title;

    private String snippet;

    private String htmlTitle;

    private String fileFormat;

    private String displayLink;

    private String htmlSnippet;
}
