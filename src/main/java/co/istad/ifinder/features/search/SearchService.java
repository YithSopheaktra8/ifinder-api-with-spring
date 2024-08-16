package co.istad.ifinder.features.search;

import co.istad.ifinder.base.BaseMessage;
import org.springframework.data.domain.Page;
import org.typesense.model.MultiSearchResult;
import org.typesense.model.SearchResultHit;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface SearchService {

    Page<Map<String, Object>> search(String searchField, int page, int size) throws Exception;
}
