package co.istad.ifinder.mapper;

import co.istad.ifinder.domain.User;
import co.istad.ifinder.features.auth.dto.RegisterRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    User mapFromRegisterCreateRequest(RegisterRequest registerRequest);


}
