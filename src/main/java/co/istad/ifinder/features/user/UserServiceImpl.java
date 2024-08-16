package co.istad.ifinder.features.user;

import co.istad.ifinder.base.BaseMessage;
import co.istad.ifinder.domain.Role;
import co.istad.ifinder.domain.User;
import co.istad.ifinder.features.user.dto.UserResponse;
import co.istad.ifinder.features.user.dto.UserUpdateRequest;
import co.istad.ifinder.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final RoleRepository roleRepository;

    @Override
    public UserResponse findOwnProfile(Jwt jwt) {

        if (jwt == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized"
            );
        }

        String email = jwt.getClaimAsString("iss");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        return userMapper.mapFromUserToUserResponse(user);
    }

    @Override
    public UserResponse findUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        if (user.getIsDelete().equals(true)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
            );
        }

        return userMapper.mapFromUserToUserResponse(user);

    }

    /**
     * @param uuid
     * @return UserResponse
     * @throws ResponseStatusException This method updates a user by uuid
     */
    @Override
    public UserResponse findUserByUuid(String uuid) {


        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with uuid " + uuid + " not found"
                ));

        if (user.getIsDelete().equals(true)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User with uuid " + uuid + " not found"
            );
        }

        return userMapper.mapFromUserToUserResponse(user);
    }


    /**
     * @param userUpdateRequest
     * @param uuid
     * @return UserResponse
     * @throws ResponseStatusException This method updates a user by uuid
     */
    @Override
    public UserResponse updateUserByUuid(String uuid, UserUpdateRequest userUpdateRequest, Jwt jwt) {

        String email = jwt.getClaimAsString("iss");


        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with uuid " + uuid + " not found"
                ));

        if (!user.getEmail().equals(email)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to update this user"
            );
        }

        if (user.getIsDelete().equals(true)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User with uuid " + uuid + " not found"
            );
        }

        userMapper.mapFromUserUpdateRequestToUser(userUpdateRequest, user);
        user = userRepository.save(user);

        return userMapper.mapFromUserToUserResponse(user);
    }


    /**
     * @param uuid
     * @return BaseMessage
     * @throws ResponseStatusException This method blocks a user by uuid
     */
    @Override
    public BaseMessage blockUserByUuid(String uuid) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with uuid " + uuid + " not found"
                ));

        if (user.getIsDelete().equals(true)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User with uuid " + uuid + " not found"
            );
        }

        user.setIsBlock(true);
        userRepository.save(user);

        return new BaseMessage("User with uuid " + uuid + " blocked successfully");
    }

    @Override
    public BaseMessage unblockUserByUuid(String uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with uuid " + uuid + " not found"
                ));

        if (user.getIsDelete().equals(true)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User with uuid " + uuid + " not found"
            );
        }

        user.setIsBlock(false);
        userRepository.save(user);

        return new BaseMessage("User with uuid " + uuid + " unBlocked successfully");
    }


    /**
     * @param uuid
     * @return BaseMessage
     * @throws ResponseStatusException This method deletes a user by uuid
     */
    @Override
    public BaseMessage deleteUserByUuid(String uuid) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with uuid " + uuid + " not found"
                ));

        if (user.getIsDelete().equals(true)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User with uuid " + uuid + " not found"
            );
        }

        user.setIsDelete(true);
        user.setEmail("");
        userRepository.save(user);

        return new BaseMessage("User with uuid " + uuid + " deleted successfully");
    }

    /**
     * @param page
     * @param size
     * @return Page<UserResponse>
     * @throws ResponseStatusException This method returns all users
     */

    @Override
    public Page<UserResponse> findAllUser(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Role not found"
                ));

        Page<User> user = userRepository.findAllByRolesAndIsDeleteFalse(List.of(role), pageRequest);
        return user.map(userMapper::mapFromUserToUserResponse);
    }

    /**
     * @param page
     * @param size
     * @return Page<UserResponse>
     * @throws ResponseStatusException This method returns all admins
     */
    @Override
    public Page<UserResponse> findAllAdmin(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Role role = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Role not found"
                ));

        Page<User> user = userRepository.findAllByRolesAndIsDeleteFalse(List.of(role), pageRequest);
        return user.map(userMapper::mapFromUserToUserResponse);
    }
}
