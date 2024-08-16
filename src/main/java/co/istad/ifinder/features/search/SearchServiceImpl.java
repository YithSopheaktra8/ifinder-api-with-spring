package co.istad.ifinder.features.search;

import co.istad.ifinder.domain.RelatedSearch;
import co.istad.ifinder.features.relatedSearch.RelatedSearchRepository;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.typesense.api.Client;
import org.typesense.model.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {

    private final Client client;
    private final RelatedSearchRepository relatedSearchRepository;

    @Override
    public Page<Map<String, Object>> search(String searchField, int page, int size) {

        List<Map<String, Object>> searchResults = new ArrayList<>();
        AtomicReference<Integer> totalResults = new AtomicReference<>(0);

        try {
            // Fetch the list of collections
            List<String> collections = Arrays.stream(client.collections().retrieve())
                    .map(CollectionResponse::getName)
                    .toList();

            // Parallel stream to search across collections
            collections.parallelStream().forEach(collection -> {
                try {
                    SearchParameters searchParameters = new SearchParameters()
                            .q(searchField)
                            .queryBy("title, keyword")
                            .page(1)
                            .perPage(30); // adjust perPage as needed

                    SearchResult searchResult = client.collections(collection).documents().search(searchParameters);

                    // Synchronize to avoid race conditions on totalResults
                    synchronized (this) {
                        totalResults.updateAndGet(v -> v + searchResult.getFound());
                    }

                    searchResults.addAll(searchResult.getHits().stream()
                            .map(SearchResultHit::getDocument)
                            .toList());

                } catch (Exception e) {
                    log.error("Error occurred while searching collection: {}", collection, e);
                }
            });

        } catch (Exception e) {
            log.error("Error occurred while fetching collections", e);
        }

        // Pagination
        int start = page * size;
        int end = Math.min(start + size, searchResults.size());

        List<Map<String, Object>> paginatedResults = (start < end) ? searchResults.subList(start, end) : new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(paginatedResults, pageRequest, totalResults.get());
    }







}
