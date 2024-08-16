package co.istad.ifinder.mapper;

import co.istad.ifinder.domain.RelatedSearch;
import co.istad.ifinder.features.relatedSearch.dto.RelatedSearchResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RelatedSearchMapper {

    RelatedSearchResponse mapFromRelatedSearch(RelatedSearch relatedSearch);

}
