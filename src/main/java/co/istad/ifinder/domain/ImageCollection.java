package co.istad.ifinder.domain;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ImageCollection {

    String url;

    List<String> imageUrl;
}
