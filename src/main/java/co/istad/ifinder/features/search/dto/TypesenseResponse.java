package co.istad.ifinder.features.search.dto;

import co.istad.ifinder.domain.ImageCollection;

public record TypesenseResponse(

         String title,

         String description,

         String author,

         String keyword,

         String logo,

         String thumbnail,

         String domain,

         ImageCollection image
) {
}
