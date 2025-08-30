package com.ddos.auth.mapper;

import com.ddos.auth.model.dto.login.LoginResponse;
import com.ddos.auth.model.dto.register.RegisterRequest;
import com.ddos.auth.model.dto.register.RegisterResponse;
import com.ddos.auth.model.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "username", expression = "java(auth != null ? auth.getUsername() : null)")
    @Mapping(target = "message", constant = "You were successfully registered.")
    RegisterResponse toRegisterResponse(Auth auth);

    @Mapping(target = "id", expression = "java(auth.getId())")
    @Mapping(target = "username", expression = "java(auth.getUsername())")
    @Mapping(target = "roles", expression = "java(auth.getRoles())")
    @Mapping(target = "jwt", source = "jwt")
    LoginResponse toLoginResponse(String jwt, Auth auth);

    @Mapping(target = "username", expression = "java(request.getUsername())")
    @Mapping(target = "roles", expression = "java(Set.of('ROLE_USER'))")
    Auth toAuth(RegisterRequest request);
}
