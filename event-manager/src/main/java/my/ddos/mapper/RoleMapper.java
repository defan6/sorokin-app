package my.ddos.mapper;

import my.ddos.model.dto.role.RoleResponse;
import my.ddos.model.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toResponse(Role entity);
}
