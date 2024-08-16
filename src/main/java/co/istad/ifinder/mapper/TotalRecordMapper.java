package co.istad.ifinder.mapper;

import co.istad.ifinder.features.auth.dto.TotalRecordResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TotalRecordMapper {

    TotalRecordResponse mapToTotalRecordResponse(Long totalUser, Long totalAdmin, Long totalFeedback, Long totalCollection);

}
