package co.istad.ifinder.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.v1.Customsearch;
import com.google.api.services.customsearch.v1.CustomsearchRequestInitializer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class GoogleCustomSearchConfig {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Value("${google.application-name}")
    private String googleAppName;

    @Value("${google.custom-search.api-key}")
    private String googleCustomSearchApiKey;

    @Bean
    public Customsearch customsearch() throws GeneralSecurityException, IOException {
        return new Customsearch.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, null)
                .setApplicationName(googleAppName)
                .setCustomsearchRequestInitializer(new CustomsearchRequestInitializer(googleCustomSearchApiKey))
                .build();
    }
}
