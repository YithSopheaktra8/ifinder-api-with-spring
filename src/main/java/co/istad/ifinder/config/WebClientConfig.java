package co.istad.ifinder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final WebClient webClient = WebClient.builder().build();

    @Bean
    public WebClient webClient() {
        return webClient;
    }

}
