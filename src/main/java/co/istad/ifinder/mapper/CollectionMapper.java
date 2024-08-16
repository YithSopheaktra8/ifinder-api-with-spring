package co.istad.ifinder.mapper;

import co.istad.ifinder.domain.Collection;
import co.istad.ifinder.domain.TypesenseCollection;
import co.istad.ifinder.schema.TypesenseDocumentSchema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CollectionMapper {

    Collection mapFromTypesenseDocument(TypesenseCollection typesenseCollection);


    @Mappings({
            @Mapping(source = "uuid", target = "uuid"),
            @Mapping(source = "fullUrl", target = "domainName"),
            @Mapping(source = "title", target = "name"),
            @Mapping(source = "icons", target = "logo"),
    })
    Collection mapFromTypesenseDocumentSchema(TypesenseDocumentSchema typesenseDocumentSchema);

}
