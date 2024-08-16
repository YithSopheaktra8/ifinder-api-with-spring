package co.istad.ifinder.features.ai;

import co.istad.ifinder.features.ai.geminiresponse.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GeminiServiceImpl implements GeminiService{

    private final WebClient webClient;

    @Value("${gemini.api-key}")
    private String API_KEY;

    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    /**
     * @param prompt
     * @return Mono<GeminiResponse>
     * This method generates content using the prompt
     */
    @Override
    public Mono<GeminiResponse> generateContent(String prompt) {

        return webClient
                .post()
                .uri(BASE_URL + "?key=" + API_KEY)
                .header("Content-Type", "application/json")
                .bodyValue("{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}]}]}")
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .doOnError(e -> System.err.println("Error occurred: " + e.getMessage()));
    }
}
