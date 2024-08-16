package co.istad.ifinder.mapper;

import co.istad.ifinder.domain.UserHistory;
import co.istad.ifinder.features.history.dto.HistoryResponse;
import com.google.api.ResourceDescriptor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

    HistoryResponse mapToHistoryResponse(UserHistory history);

}
