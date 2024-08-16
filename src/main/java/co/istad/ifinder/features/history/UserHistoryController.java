package co.istad.ifinder.features.history;


import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.UserHistory;
import co.istad.ifinder.features.history.dto.AddHistoryRequest;
import co.istad.ifinder.features.history.dto.DeleteHistoryRequest;
import co.istad.ifinder.features.history.dto.HistoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-history")
public class UserHistoryController {

    private final UserHistoryService userHistoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseMessage addHistory(@RequestBody @Valid AddHistoryRequest addHistoryRequest,
                                  @AuthenticationPrincipal Jwt jwt){

        return userHistoryService.addHistory(addHistoryRequest, jwt);
    }

    @GetMapping
    public Page<HistoryResponse> getUserHistory(
            @RequestParam String uuid,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "5", required = false) int size,
            @RequestParam(defaultValue = "desc", required = false) String sortOrder) {

        return userHistoryService.findAllUserHistory(uuid, page, size, sortOrder);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseMessage deleteHistory(@AuthenticationPrincipal Jwt jwt,
                                     @Valid @RequestBody DeleteHistoryRequest deleteHistoryRequest){

        return userHistoryService.deleteHistory(deleteHistoryRequest, jwt);
    }

}
