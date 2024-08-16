package co.istad.ifinder.domain;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TypesenseCollection {

    private Integer id;

    private String title;

    private String description;

    private String author;

    private String keyword;

    private String logo;

    private String thumbnail;

    private String domain;

    private ImageCollection image;
}
