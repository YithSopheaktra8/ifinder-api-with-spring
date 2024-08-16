package co.istad.ifinder.features.ai;

import co.istad.ifinder.features.ai.geminiresponse.GeminiResponse;
import reactor.core.publisher.Mono;

public interface GeminiService {

    Mono<GeminiResponse> generateContent(String query);
}
