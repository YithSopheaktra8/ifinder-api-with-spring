package co.istad.ifinder.schema;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TypesenseDocumentSchema {
    private String uuid;

    private String fullUrl;

    private String title;

    private String description;

    private String keywords;

    private String ogTitle;

    private String ogDescription;

    private String thumbnail;

    private String ogSiteName;

    private String icons;

    private List<String> images;

    private RelatedLink relatedLink;
}
