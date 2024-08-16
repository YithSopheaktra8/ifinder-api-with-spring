package co.istad.ifinder.features.scrap;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.TypesenseCollection;
import co.istad.ifinder.features.search.dto.DataImportRequest;
import co.istad.ifinder.schema.TypesenseDocumentSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/web-scraper")
public class WebScraperController {

    private final WebScraperService webScraperService;

    @PostMapping("/scrape")
    public TypesenseDocumentSchema scrape(@RequestBody DataImportRequest dataImportRequest) {

        try {
            return webScraperService.getScrapeData(dataImportRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/dataImport")
    public Map<String, Object> dataImport(@RequestBody DataImportRequest dataImportRequest) {

        try {
            return webScraperService.getDataImport(dataImportRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/changeCategory")
    public BaseMessage changeCategory(@RequestBody String category) {

        return webScraperService.changeCategoryWebsite(category);
    }

    @PostMapping("/autoScrapeAction")
    public BaseMessage startAutoScrape(@RequestParam(required = false) Boolean start,
                                       @RequestParam(required = false) Boolean stop){

        try {
            return webScraperService.autoScrapeAction(start,stop);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
