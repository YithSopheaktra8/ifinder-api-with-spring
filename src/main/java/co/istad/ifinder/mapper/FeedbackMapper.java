package co.istad.ifinder.mapper;

import co.istad.ifinder.domain.Feedback;
import co.istad.ifinder.features.feedback.dto.FeedbackResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    @Mappings({
            @Mapping(source = "user.profileImage", target = "profileImage"),
            @Mapping(target = "firstName", source = "user.firstName"),
            @Mapping(target = "lastName", source = "user.lastName"),
    })
    FeedbackResponse mapFromFeedbackToFeedbackResponse(Feedback feedback);

}
