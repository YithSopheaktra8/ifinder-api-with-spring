package co.istad.ifinder.features.history;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.Collection;
import co.istad.ifinder.domain.User;
import co.istad.ifinder.domain.UserHistory;
import co.istad.ifinder.features.collection.CollectionRepository;
import co.istad.ifinder.features.history.dto.AddHistoryRequest;
import co.istad.ifinder.features.history.dto.DeleteHistoryRequest;
import co.istad.ifinder.features.history.dto.HistoryResponse;
import co.istad.ifinder.features.user.UserRepository;
import co.istad.ifinder.features.user.dto.UserResponse;
import co.istad.ifinder.mapper.HistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserHistoryServiceImpl implements UserHistoryService {

    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final HistoryMapper historyMapper;
    private final CollectionRepository collectionRepository;


    /**
     * @param addHistoryRequest
     * @return BaseMessage
     * @throws ResponseStatusException
     * @since 20/May/2024
     * This method adds a history for a user
     */
    @Transactional
    @Override
    public BaseMessage addHistory(AddHistoryRequest addHistoryRequest, Jwt jwt) {

        String email = jwt.getClaimAsString("iss");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        if(!user.getUuid().equals(addHistoryRequest.userUuid())){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to add history on this user account"
            );
        }

        Collection collection = collectionRepository.findByDomainName(addHistoryRequest.url())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Collection not found"
                ));

        UserHistory userHistory = new UserHistory();
        userHistory.setUuid(UUID.randomUUID().toString());
        userHistory.setUrl(addHistoryRequest.url());
        userHistory.setUser(user);
        userHistory.setIcon(collection.getLogo());
        userHistory.setTitle(collection.getName());
        userHistoryRepository.save(userHistory);

        return BaseMessage.builder()
                .message("History has been added")
                .build();
    }


    /**
     * @param uuid
     * @param page
     * @param size
     * @param sortOrder
     * @return Page<UserHistory>
     * This method returns all user history
     */
    @Override
    public Page<HistoryResponse> findAllUserHistory(String uuid, int page, int size, String sortOrder) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by("lastModifiedAt").ascending() : Sort.by("lastModifiedAt").descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return userHistoryRepository.findAllByUser(pageable, user)
                .map(historyMapper::mapToHistoryResponse);
    }

    @Override
    public BaseMessage deleteHistory(DeleteHistoryRequest deleteHistoryRequest, Jwt jwt) {

        User user = userRepository.findByEmail(jwt.getClaimAsString("iss"))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        if(!user.getUuid().equals(deleteHistoryRequest.userUuid())){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to add history on this user account"
            );
        }

        UserHistory userHistory = userHistoryRepository.findByUuid(deleteHistoryRequest.historyUuid())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "History not found"
                ));

        userHistoryRepository.delete(userHistory);

        return BaseMessage.builder()
                .message("History has been deleted")
                .build();
    }
}
