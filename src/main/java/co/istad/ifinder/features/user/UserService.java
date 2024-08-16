package co.istad.ifinder.features.user;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.User;
import co.istad.ifinder.features.user.dto.UserResponse;
import co.istad.ifinder.features.user.dto.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface UserService {

    UserResponse findOwnProfile(Jwt jwt);

    UserResponse findUserByEmail(String email);

    UserResponse findUserByUuid(String uuid);

    UserResponse updateUserByUuid(String uuid, UserUpdateRequest userUpdateRequest, Jwt jwt);

    BaseMessage blockUserByUuid(String uuid);

    BaseMessage unblockUserByUuid(String uuid);

    BaseMessage deleteUserByUuid(String uuid);

    Page<UserResponse> findAllUser(int page, int size);

    Page<UserResponse> findAllAdmin(int page, int size);

}
