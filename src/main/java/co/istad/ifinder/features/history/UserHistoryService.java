package co.istad.ifinder.features.history;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.UserHistory;
import co.istad.ifinder.features.history.dto.AddHistoryRequest;
import co.istad.ifinder.features.history.dto.DeleteHistoryRequest;
import co.istad.ifinder.features.history.dto.HistoryResponse;
import co.istad.ifinder.features.user.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserHistoryService {

    BaseMessage addHistory(AddHistoryRequest addHistoryRequest, Jwt jwt);

    Page<HistoryResponse> findAllUserHistory(String uuid, int page, int size, String sortOrder);

    BaseMessage deleteHistory(DeleteHistoryRequest deleteHistoryRequest, Jwt jwt);

}
