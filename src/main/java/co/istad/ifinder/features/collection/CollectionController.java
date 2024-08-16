package co.istad.ifinder.features.collection;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.features.collection.dto.CollectionDtoResponse;
import co.istad.ifinder.features.search.dto.TypesenseCreateRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/collection")
@RequiredArgsConstructor
@SecurityRequirement(name = "accessToken")
public class CollectionController {

    private final CollectionService collectionService;

    @GetMapping
    public CollectionDtoResponse collection(@RequestParam String name) {

        return collectionService.findCollection(name);
    }

    @GetMapping("/all")
    public Page<CollectionDtoResponse> collections(@RequestParam( required = false, defaultValue = "0") int page,
                                                   @RequestParam( required = false,defaultValue = "5") int size) {

        return collectionService.findAllCollections(page, size);
    }

    @DeleteMapping("/{uuid}/delete")
    @ResponseStatus(HttpStatus.OK)
    public BaseMessage deleteCollection(@PathVariable String uuid) throws Exception {

        return collectionService.deleteCollection(uuid);
    }


    @DeleteMapping("/deleteAll")
    public BaseMessage deleteAllCollections() throws Exception {
        return collectionService.deleteAllCollections();
    }
}
