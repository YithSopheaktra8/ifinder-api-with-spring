package co.istad.ifinder.features.ai;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    @GetMapping("/search")
    public Mono<String> search(@RequestParam String prompt) {
        return geminiService.generateContent(prompt)
                .map(geminiResponse -> geminiResponse.getCandidates().get(0).getContent().getParts().get(0).getText());
    }

}
