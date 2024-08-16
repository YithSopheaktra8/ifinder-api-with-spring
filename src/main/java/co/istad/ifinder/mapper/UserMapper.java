package co.istad.ifinder.mapper;

import co.istad.ifinder.domain.User;
import co.istad.ifinder.features.user.dto.UserResponse;
import co.istad.ifinder.features.user.dto.UserUpdateRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse mapFromUserToUserResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User mapFromUserUpdateRequestToUser(UserUpdateRequest userUpdateRequest, @MappingTarget User user);

}
