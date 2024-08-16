package co.istad.ifinder.features.search.dto;

import java.util.List;

public record TypesenseCreateRequest(

        String title,

        String description,

        String author,

        String keyword,

        String logo,

        String thumbnail,

        String domain,

        List<String> image
) {
}
