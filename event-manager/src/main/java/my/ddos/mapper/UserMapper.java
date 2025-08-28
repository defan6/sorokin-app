package my.ddos.mapper;

import my.ddos.model.dto.user.UserResponse;
import my.ddos.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, BookingMapper.class})
public interface UserMapper {

    @Mapping(target = "userRoles", source = "userRoles")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "bookings", source = "bookings")
    UserResponse toResponse(User entity);
}
