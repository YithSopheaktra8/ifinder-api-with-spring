package co.istad.ifinder.features.feedback;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.Feedback;
import co.istad.ifinder.domain.User;
import co.istad.ifinder.features.feedback.dto.FeedbackRequest;
import co.istad.ifinder.features.feedback.dto.FeedbackResponse;
import co.istad.ifinder.features.user.UserRepository;
import co.istad.ifinder.mapper.FeedbackMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService{

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final FeedbackMapper feedbackMapper;


    /**
     * @param feedbackRequest
     * @return BaseMessage
     * @throws ResponseStatusException
     * @since 20/May/2024
     * This method creates a feedback for a user
     */
    @Override
    public BaseMessage createFeedback(FeedbackRequest feedbackRequest) {

        User user = userRepository.findByUuid(feedbackRequest.userUuid())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                        )
                );

        Feedback feedback = new Feedback();
        feedback.setUuid(UUID.randomUUID().toString());
        feedback.setFeedback(feedbackRequest.feedback());
        feedback.setUser(user);
        feedbackRepository.save(feedback);

        return BaseMessage.builder()
                .message("Feedback submit successfully")
                .build();
    }


    /**
     * @param page
     * @param size
     * @return Page<FeedbackResponse>
     * This method returns all feedbacks
     */
    @Override
    public Page<FeedbackResponse> findAllFeedbacks(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Feedback> feedbacks = feedbackRepository.findAll(pageRequest);

        return feedbacks.map(feedbackMapper::mapFromFeedbackToFeedbackResponse);
    }


    /**
     * @param uuid
     * @return BaseMessage
     * This method deletes a feedback
     */
    @Override
    public BaseMessage deleteFeedback(String uuid) {

        Feedback feedback = feedbackRepository.findByUuid(uuid)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Feedback has not been found"
                ));

        feedbackRepository.delete(feedback);

        return BaseMessage.builder()
                .message("Feedback has been delete successfully")
                .build();
    }
}
