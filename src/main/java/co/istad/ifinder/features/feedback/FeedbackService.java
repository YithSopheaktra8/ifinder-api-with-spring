package co.istad.ifinder.features.feedback;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.features.feedback.dto.FeedbackRequest;
import co.istad.ifinder.features.feedback.dto.FeedbackResponse;
import org.springframework.data.domain.Page;

public interface FeedbackService {

    BaseMessage createFeedback(FeedbackRequest feedbackRequest);

    Page<FeedbackResponse> findAllFeedbacks(int page, int size);

    BaseMessage deleteFeedback(String uuid);



}
