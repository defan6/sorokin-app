package my.ddos.mapper;

import my.ddos.model.dto.user.UserResponse;
import my.ddos.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, BookingMapper.class})
public interface UserMapper {
    UserResponse toResponse(User entity);
}
