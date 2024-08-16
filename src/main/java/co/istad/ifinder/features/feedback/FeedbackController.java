package co.istad.ifinder.features.feedback;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.features.feedback.dto.FeedbackRequest;
import co.istad.ifinder.features.feedback.dto.FeedbackResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public BaseMessage createFeedback(@Valid @RequestBody FeedbackRequest feedbackRequest) {

        return feedbackService.createFeedback(feedbackRequest);
    }

    @GetMapping
    public Page<FeedbackResponse> findAllFeedbacks(@RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "5") int size) {

        return feedbackService.findAllFeedbacks(page, size);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{uuid}/delete")
    public BaseMessage deleteFeedback(@PathVariable String uuid) {

        return feedbackService.deleteFeedback(uuid);
    }

}
