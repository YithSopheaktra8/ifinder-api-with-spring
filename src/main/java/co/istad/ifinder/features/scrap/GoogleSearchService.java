package co.istad.ifinder.features.scrap;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.v1.Customsearch;
import com.google.api.services.customsearch.v1.CustomsearchRequestInitializer;
import com.google.api.services.customsearch.v1.model.Result;
import com.google.api.services.customsearch.v1.model.Search;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.google.api.client.json.JsonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleSearchService {

    private static final String APPLICATION_NAME = "ifinder";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String YOUR_API_KEY = "AIzaSyA7HkXbnn7s5x0-qMhEXhM2EAHxu1pXgAI";
    private static final String SEARCH_ENGINE_ID = "86ccfdf067c2c4d43";

    @Value("${google.application-name}")
    private String googleAppName;

    private Customsearch customsearch;


    private final Set<String> fetchedUrls = new HashSet<>();
    private int startIndex = 1; // Initialize the start index for pagination


    @PostConstruct
    public void init() throws GeneralSecurityException, IOException {
        this.customsearch = new Customsearch.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .setCustomsearchRequestInitializer(new CustomsearchRequestInitializer(YOUR_API_KEY))
                .build();
    }


//    @Scheduled(fixedRate = 30000) // 30 seconds in milliseconds
    public void search() throws IOException {
        String query = "(site:.kh)";

        // Create the search request with the query and start index
        Customsearch.Cse.List list = customsearch.cse().list().setQ(query)
                .setCx(SEARCH_ENGINE_ID)
                .setStart((long) startIndex)
                .setSearchType("image");

        // Execute the search
        Search search = list.execute();

        // Get the search results
        List<Result> results = search.getItems();

        // Process the results
        if (results != null) {
            for (Result result : results) {
                String displayLink = result.getDisplayLink();
                if (!fetchedUrls.contains(displayLink)) {
                    fetchedUrls.add(displayLink);
                    // Do something with the link
                    System.out.println("New Link: " + displayLink);
                }
            }
        }

        // Update the start index for the next search
        startIndex += 10; // Assuming 10 results per page
        if (startIndex > 90) { // Reset after 90 to avoid exceeding API limits
            startIndex = 1;
        }
    }
}
