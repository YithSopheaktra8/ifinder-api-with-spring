package co.istad.ifinder.features.collection;


import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.Collection;
import co.istad.ifinder.domain.ImageCollection;
import co.istad.ifinder.domain.TypesenseCollection;
import co.istad.ifinder.features.collection.dto.CollectionDtoResponse;
import co.istad.ifinder.features.search.dto.TypesenseCreateRequest;
import co.istad.ifinder.json.JsonToObject;
import co.istad.ifinder.schema.TypesenseDocumentSchema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.typesense.api.Client;
import org.typesense.api.FieldTypes;
import org.typesense.model.CollectionResponse;
import org.typesense.model.CollectionSchema;
import org.typesense.model.Field;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionServiceImpl implements CollectionService {

    private final JsonToObject jsonToObject;
    private final CollectionRepository collectionRepository;
    private final ObjectMapper objectMapper;
    private final Client client;

    /**
     * @param documentMap
     * @param typesenseDocumentSchema
     * @param documentMap         This method saves the collection in the database
     */
    @Override
    public void saveCollection(TypesenseDocumentSchema typesenseDocumentSchema, Map<String, Object> documentMap) {
        Collection collection = new Collection();
        collection.setName(typesenseDocumentSchema.getTitle());
        collection.setUuid(UUID.randomUUID().toString());
        collection.setDescription(typesenseDocumentSchema.getDescription());
        collection.setThumbnail(typesenseDocumentSchema.getThumbnail());
        collection.setDomainName(typesenseDocumentSchema.getFullUrl());
        collection.setLogo(typesenseDocumentSchema.getIcons());

        try {
            String documentsJson = objectMapper.writeValueAsString(documentMap);
            collection.setDocuments(documentsJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle error appropriately, e.g., logging or rethrowing as a runtime exception
        }

        collectionRepository.save(collection);
    }


    /**
     * @param title
     * @return CollectionDtoResponse
     * @throws ResponseStatusException This method finds the collection by title
     */
    @Override
    public CollectionDtoResponse findCollection(String title) {

        Collection collection = collectionRepository.findByNameContainsIgnoreCase(title)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Collection not found"));

        String json = collection.getDocuments();
        TypesenseCollection typesenseCollection = jsonToObject.convert(json, TypesenseCollection.class);

        return new CollectionDtoResponse(collection, typesenseCollection);
    }


    /**
     * @param page
     * @param size
     * @return Page<CollectionDtoResponse>
     * This method finds all collections
     */
    @Override
    public Page<CollectionDtoResponse> findAllCollections(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Collection> collections = collectionRepository.findAll(pageRequest);

        return collections.map(collection -> {
            String json = collection.getDocuments();
            TypesenseCollection typesenseCollection = jsonToObject.convert(json, TypesenseCollection.class);
            return new CollectionDtoResponse(collection, typesenseCollection);
        });
    }


    /**
     * @param uuid
     * @return BaseMessage
     * @throws Exception This method deletes a collection
     */
    @Override
    public BaseMessage deleteCollection(String uuid) throws Exception {

        Collection collection = collectionRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Collection not found"
                ));

        client.collections(collection.getName()).delete();
        collectionRepository.delete(collection);

        return BaseMessage.builder()
                .message("Collection has been deleted")
                .build();
    }

    /**
     * @return BaseMessage
     * @throws Exception This method deletes all collections
     * This method deletes all collections
     */
    @Override
    public BaseMessage deleteAllCollections() throws Exception {

        org.typesense.api.Collections collection = client.collections();
        CollectionResponse[] collections = collection.retrieve();

        for (CollectionResponse collectionResponse : collections) {
            String collectionName = collectionResponse.getName();
            client.collections(collectionName).delete();
        }

        collectionRepository.deleteAll();

        return BaseMessage.builder()
                .message("All collections have been deleted")
                .build();
    }

}
