package co.istad.ifinder.features.scrap;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.Collection;
import co.istad.ifinder.domain.ScrapUrl;
import co.istad.ifinder.domain.ScrapeProcessStatus;
import co.istad.ifinder.features.collection.CollectionRepository;
import co.istad.ifinder.features.collection.CollectionService;
import co.istad.ifinder.features.search.dto.DataImportRequest;
import co.istad.ifinder.mapper.CollectionMapper;
import co.istad.ifinder.schema.ResultSchema;
import co.istad.ifinder.schema.TypesenseDocumentSchema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.customsearch.v1.Customsearch;
import com.google.api.services.customsearch.v1.model.Result;
import com.google.api.services.customsearch.v1.model.Search;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.typesense.api.Client;
import org.typesense.api.FieldTypes;
import org.typesense.model.CollectionSchema;
import org.typesense.model.Field;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class WebScraperServiceImpl implements WebScraperService {

    private final ScrapUrlRepository scrapUrlRepository;
    private final CollectionMapper collectionMapper;
    private final Client client;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final CollectionRepository collectionRepository;
    private final Customsearch customsearch;
    private final ScrapeProcessStatusRepository processingStatusRepository;
    private final CollectionService collectionService;
    @Value("${typesense.port}")
    String typesensePort;
    @Value("${typesense.apiKey}")
    String typesenseApiKey;
    @Value("${google.custom-search.engine-id}")
    String googleAppId;

    @Value("${scrape.express-js-url}")
    String scrapeExpressJsUrl;

    private int startIndex = 1; // Initialize the start index


    /**
     * @throws Exception This method scrapes the website
     */
    @Override
    @Scheduled(initialDelay = 60000, fixedRate = 7200000) // 2 hour it means a day it runs 12 times . 30mn delay
    public void scrapeWebsite() throws Exception {

        ScrapeProcessStatus processingStatus = processingStatusRepository.findById(1).orElseThrow();

        if (!processingStatus.getIsRunning()) {
            return;
        }

        int lastProcessedId = processingStatus.getLastProcessedId();

        List<ScrapUrl> scrapUrl = scrapUrlRepository.findByIdGreaterThanOrderByIdAsc(lastProcessedId);
        if (scrapUrl.isEmpty()) {
            log.info("No new website to scrape");
            return;
        }

        for (ScrapUrl scrap : scrapUrl) {
            try {
                processScrapUrl(scrap);
                processingStatus.setLastProcessedId(scrap.getId());
                processingStatusRepository.save(processingStatus);
            } catch (Exception e) {
                log.error("Error processing ScrapUrl with ID {}: {}", scrap.getId(), e.getMessage());
            }
        }
    }

    /**
     * @throws IOException This method gets the website URL
     *                     using with spring sheduling
     */
    @Scheduled(fixedRate = 21600000) // 6 hour it means a day it runs 4 times
//    @Scheduled(fixedRate = 300000)
    @Override
    public void getWebsiteUrl() throws IOException {

        ScrapeProcessStatus scrapeProcessStatus = processingStatusRepository.findById(1).orElseThrow();

        if (!scrapeProcessStatus.getIsRunning()) {
            return;
        }

        String category = scrapeProcessStatus.getCategoryName();

        String query = "(site:.kh) " + category;

        // Create the search request with the query and start index
        Customsearch.Cse.List list = customsearch.cse().list().setQ(query)
                .setCx(googleAppId)
                .setStart((long) startIndex)
                .setSearchType("image");

        // Execute the search
        Search search = list.execute();

        // Get the search results
        List<Result> results = search.getItems();

        // Process the results
        if (results != null) {
            for (Result result : results) {
                ResultSchema resultSchema = objectMapper.readValue(result.toString(), ResultSchema.class);
                String stringUrl = result.getImage().getContextLink();
                try {
                    URL url = new URL(stringUrl);
                    String protocol = url.getProtocol();
                    String host = url.getHost();
                    if (host.endsWith(".kh")) {
                        stringUrl = protocol + "://" + host;
                    } else {
                        System.out.println("No domain ending with .kh found.");
                    }
                } catch (MalformedURLException e) {
                    System.out.println("Invalid URL: " + e.getMessage());
                }
                // Check if the URL is valid and not already fetched
                ScrapUrl scrapUrl = scrapUrlRepository.findByDomain(stringUrl);
                if (scrapUrl == null) {
                    ScrapUrl scrap = ScrapUrl.builder()
                            .domain(stringUrl)
                            .urlsData(objectMapper.writeValueAsString(resultSchema))
                            .build();
                    scrapUrlRepository.save(scrap);
                } else if (!stringUrl.equals(scrapUrl.getDomain())) {
                    ScrapUrl scrap = ScrapUrl.builder()
                            .domain(stringUrl)
                            .urlsData(objectMapper.writeValueAsString(resultSchema))
                            .build();
                    scrapUrlRepository.save(scrap);
                }
            }
        }
        // Update the start index for the next search
        startIndex += 10; // Assuming 10 results per page
    }


    /**
     * @throws IOException This method scrapes the website
     */
    @Scheduled(initialDelay = 3600000, fixedRate = 21600000) // 6 hour it means a day it runs 4 times . 1 hours delay
//    @Scheduled( fixedRate = 420000) // 6 hour it means a day it runs 4 times . 1 hours delay
    @Override
    public void spiderScrape() throws IOException {

        ScrapeProcessStatus scrapeProcessStatus = processingStatusRepository.findById(1).orElseThrow();

        if (!scrapeProcessStatus.getIsRunning()) {
            return;
        }

        int lastSpiderProcessedId = scrapeProcessStatus.getLastSpiderProcessedId();

        List<ScrapUrl> scrapUrls = scrapUrlRepository.findByIdGreaterThanOrderByIdAsc(lastSpiderProcessedId);

        if (scrapUrls.isEmpty()) {
            return;
        }

        List<ScrapUrl> scrapUrlList = new ArrayList<>();
        for (ScrapUrl scrapUrl : scrapUrls) {
            ResultSchema resultSchema = objectMapper.readValue(scrapUrl.getUrlsData(), ResultSchema.class);
            String json = webClient.get()
                    .uri(scrapeExpressJsUrl + "scrape?url=" + scrapUrl.getDomain())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Block until the result is available (synchronous)

            TypesenseDocumentSchema typesenseDocumentSchema = objectMapper.readValue(json, TypesenseDocumentSchema.class);

            for (String website : typesenseDocumentSchema.getRelatedLink().getWebsites()) {
                if (!scrapUrlRepository.existsByDomain(website)) {
                    ScrapUrl scrap = ScrapUrl.builder()
                            .domain(website)
                            .urlsData(objectMapper.writeValueAsString(resultSchema))
                            .build();
                    scrapUrlList.add(scrap);
                }
            }
            // Save all new ScrapUrls
            if (!scrapUrlList.isEmpty()) {
                scrapUrlRepository.saveAll(scrapUrlList);
            }

            // Update the last processed ID
            if (!scrapUrls.isEmpty()) {
                scrapeProcessStatus.setLastSpiderProcessedId(scrapUrls.getLast().getId());
                processingStatusRepository.save(scrapeProcessStatus);
            }
        }
    }

    /**
     * @param scrapUrl
     * @throws Exception This method processes the page
     */
    @Override
    public void processScrapUrl(ScrapUrl scrapUrl) throws Exception {

        ResultSchema resultSchema = objectMapper.readValue(scrapUrl.getUrlsData(), ResultSchema.class);
        String json = webClient.get()
                .uri(scrapeExpressJsUrl + "scrape?url=" + scrapUrl.getDomain())
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Block until the result is available (synchronous)

        TypesenseDocumentSchema typesenseDocumentSchema = objectMapper.readValue(json, TypesenseDocumentSchema.class);
        typesenseDocumentSchema.setUuid(UUID.randomUUID().toString());
        typesenseDocumentSchema.setThumbnail(resultSchema.getLink());

        // Collection and document processing (omitted for brevity)
        CollectionSchema collectionSchema = new CollectionSchema();
        collectionSchema.name(typesenseDocumentSchema.getTitle())
                .addFieldsItem(new Field().name(".*").type(FieldTypes.AUTO).optional(true))
                .addFieldsItem(new Field().name("description").type(FieldTypes.STRING).sort(true))
                .enableNestedFields(true);

        client.collections().create(collectionSchema);

        HashMap<String, Object> documentList = new HashMap<>();
        documentList.put("uuid", typesenseDocumentSchema.getUuid());
        documentList.put("fullUrl", typesenseDocumentSchema.getFullUrl());
        documentList.put("title", typesenseDocumentSchema.getTitle());
        documentList.put("description", typesenseDocumentSchema.getDescription());
        documentList.put("keyword", typesenseDocumentSchema.getKeywords());
        documentList.put("ogTitle", typesenseDocumentSchema.getOgTitle());
        documentList.put("ogDescription", typesenseDocumentSchema.getOgDescription());
        documentList.put("thumbnail", resultSchema.getLink());
        documentList.put("ogSiteName", typesenseDocumentSchema.getOgSiteName());
        documentList.put("icons", typesenseDocumentSchema.getIcons());
        documentList.put("images", typesenseDocumentSchema.getImages());
        documentList.put("relatedLink", typesenseDocumentSchema.getRelatedLink());

        client.collections(typesenseDocumentSchema.getTitle()).documents().create(documentList);

        Collection collection = collectionMapper.mapFromTypesenseDocumentSchema(typesenseDocumentSchema);
        collection.setUuid(typesenseDocumentSchema.getUuid());
        collection.setDocuments(json);
        collectionRepository.save(collection);

    }


    /**
     * @param dataImportRequest
     * @return Map<String, Object>
     * @throws Exception This method imports data from a website
     */
    @Override
    public Map<String, Object> getDataImport(DataImportRequest dataImportRequest) throws Exception {

        String json = webClient.get()
                .uri(scrapeExpressJsUrl + "scrape?url=" + dataImportRequest.linkUri())
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Block until the result is available (synchronous)

        TypesenseDocumentSchema typesenseDocumentSchema = objectMapper.readValue(json, TypesenseDocumentSchema.class);
        typesenseDocumentSchema.setUuid(UUID.randomUUID().toString());

        CollectionSchema collectionSchema = new CollectionSchema();
        collectionSchema.name(typesenseDocumentSchema.getTitle())
                .addFieldsItem(new Field().name(".*").type(FieldTypes.AUTO).optional(true))
                .addFieldsItem(new Field().name("description").type(FieldTypes.STRING).sort(true))
                .enableNestedFields(true);

        // create collection in typesense
        client.collections().create(collectionSchema);

        // create document value in typesense
        HashMap<String, Object> documentList = new HashMap<>();
        documentList.put("uuid", typesenseDocumentSchema.getUuid());
        documentList.put("fullUrl", typesenseDocumentSchema.getFullUrl());
        documentList.put("title", typesenseDocumentSchema.getTitle());
        documentList.put("description", typesenseDocumentSchema.getDescription());
        documentList.put("keyword", typesenseDocumentSchema.getKeywords());
        documentList.put("ogTitle", typesenseDocumentSchema.getOgTitle());
        documentList.put("ogDescription", typesenseDocumentSchema.getOgDescription());
        documentList.put("thumbnail", typesenseDocumentSchema.getThumbnail());
        documentList.put("ogSiteName", typesenseDocumentSchema.getOgSiteName());
        documentList.put("icons", typesenseDocumentSchema.getIcons());
        documentList.put("images", typesenseDocumentSchema.getImages());
        documentList.put("relatedLink", typesenseDocumentSchema.getRelatedLink());

        // create document in typesense
        client.collections(typesenseDocumentSchema.getTitle()).documents().create(documentList);
        Map<String, Object> documents = client.collections(typesenseDocumentSchema.getTitle()).documents("0").retrieve();
        collectionService.saveCollection(typesenseDocumentSchema, documents);

        return documents;
    }

    /**
     * @param dataImportRequest
     * @return TypesenseDocumentSchema
     * @throws IOException This method scrapes the data
     */
    @Override
    public TypesenseDocumentSchema getScrapeData(DataImportRequest dataImportRequest) throws IOException {

        String json = webClient.get()
                .uri(scrapeExpressJsUrl + "scrape?url=" + dataImportRequest.linkUri())
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Block until the result is available (synchronous)

        TypesenseDocumentSchema typesenseDocumentSchema = objectMapper.readValue(json, TypesenseDocumentSchema.class);
        typesenseDocumentSchema.setUuid(UUID.randomUUID().toString());

        return typesenseDocumentSchema;
    }

    /**
     * @param category
     * @return BaseMessage
     * This method changes the category of the website
     */
    @Override
    public BaseMessage changeCategoryWebsite(String category) {

        ScrapeProcessStatus scrapeProcessStatus = processingStatusRepository.findById(1).orElseThrow();
        scrapeProcessStatus.setCategoryName(category);

        processingStatusRepository.save(scrapeProcessStatus);
        return BaseMessage.builder()
                .message("Category changed successfully")
                .build();
    }

    /**
     * @param start
     * @param stop
     * @return BaseMessage
     * @throws Exception This method starts or stops the auto scrape
     */
    @Override
    public BaseMessage autoScrapeAction(Boolean start, Boolean stop) throws Exception {
        ScrapeProcessStatus scrapeProcessStatus = processingStatusRepository.findById(1).orElseThrow();

        if (Boolean.TRUE.equals(start) && Boolean.TRUE.equals(stop)) {
            return BaseMessage.builder()
                    .message("Invalid request: both start and stop cannot be true at the same time.")
                    .build();
        } else if (Boolean.TRUE.equals(start)) {
            scrapeProcessStatus.setIsRunning(true);
            processingStatusRepository.save(scrapeProcessStatus);
            return BaseMessage.builder()
                    .message("Auto scrape started")
                    .build();
        } else if (Boolean.TRUE.equals(stop)) {
            scrapeProcessStatus.setIsRunning(false);
            processingStatusRepository.save(scrapeProcessStatus);
            return BaseMessage.builder()
                    .message("Auto scrape stopped")
                    .build();
        } else {
            return BaseMessage.builder()
                    .message("No action taken: both start and stop are false.")
                    .build();
        }
    }


}
