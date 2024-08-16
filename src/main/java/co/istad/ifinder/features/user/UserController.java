package co.istad.ifinder.features.user;


import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.features.user.dto.UserResponse;
import co.istad.ifinder.features.user.dto.UserUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{uuid}")
    public UserResponse findUserByUuid(@PathVariable String uuid){
        return userService.findUserByUuid(uuid);
    }

    @PatchMapping("/{uuid}")
    public UserResponse updateUser(@PathVariable String uuid,
                                   @RequestBody UserUpdateRequest userUpdateRequest,
                                   @AuthenticationPrincipal Jwt jwt){
        return userService.updateUserByUuid(uuid, userUpdateRequest, jwt);
    }

    @PutMapping("/{uuid}/block")
    public BaseMessage blockUser(@PathVariable String uuid){
        return userService.blockUserByUuid(uuid);
    }

    @PutMapping("/{uuid}/unblock")
    public BaseMessage unblockUser(@PathVariable String uuid){
        return userService.unblockUserByUuid(uuid);
    }

    @PutMapping("/{uuid}/delete")
    public BaseMessage deleteUser(@PathVariable String uuid){
        return userService.deleteUserByUuid(uuid);
    }

    @GetMapping("/all")
    public Page<UserResponse> findAllUser(@RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "10") int size){
        return userService.findAllUser(page, size);
    }

    @GetMapping("/all/admins")
    public Page<UserResponse> findAllAdmin(@RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "10") int size){
        return userService.findAllAdmin(page, size);
    }

    @GetMapping("/{email}/email")
    public UserResponse findUserByEmail(@PathVariable String email){
        return userService.findUserByEmail(email);
    }

    @GetMapping("/me")
    public UserResponse findOwnProfile(@AuthenticationPrincipal Jwt jwt){
        return userService.findOwnProfile(jwt);
    }

}
