package co.istad.ifinder.features.collection.dto;

import co.istad.ifinder.domain.Collection;
import co.istad.ifinder.domain.TypesenseCollection;


public record CollectionDtoResponse(

        Collection collection,

        TypesenseCollection typesenseCollection
) {
}
