package co.istad.ifinder.features.collection;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.TypesenseCollection;
import co.istad.ifinder.features.collection.dto.CollectionDtoResponse;
import co.istad.ifinder.features.search.dto.TypesenseCreateRequest;
import co.istad.ifinder.schema.TypesenseDocumentSchema;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CollectionService {

    void saveCollection(TypesenseDocumentSchema typesenseDocumentSchema, Map<String,Object> documentMap);

    CollectionDtoResponse findCollection(String title);

    Page<CollectionDtoResponse> findAllCollections(int page, int size);

    BaseMessage deleteCollection(String uuid) throws Exception;

    BaseMessage deleteAllCollections() throws Exception;
}
