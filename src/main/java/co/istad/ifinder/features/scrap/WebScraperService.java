package co.istad.ifinder.features.scrap;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.ScrapUrl;
import co.istad.ifinder.domain.TypesenseCollection;
import co.istad.ifinder.features.search.dto.DataImportRequest;
import co.istad.ifinder.schema.TypesenseDocumentSchema;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface WebScraperService {

    void scrapeWebsite() throws Exception;

    void getWebsiteUrl() throws IOException;

    void spiderScrape() throws IOException;

    void processScrapUrl(ScrapUrl scrapUrls) throws Exception;

    Map<String, Object> getDataImport(DataImportRequest dataImportRequest) throws Exception;

    TypesenseDocumentSchema getScrapeData(DataImportRequest dataImportRequest) throws IOException;

    BaseMessage changeCategoryWebsite(String category);

    BaseMessage autoScrapeAction(Boolean start, Boolean stop) throws Exception;

}
