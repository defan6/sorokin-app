package com.ddos.profile.mapper;

import com.ddos.profile.dto.ProfileResponse;
import com.ddos.profile.dto.UpdateProfileRequest;
import com.ddos.profile.model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileDtoMapper {
    ProfileResponse toResponse(Profile profile);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateProfileFromRequest(UpdateProfileRequest request, @MappingTarget Profile profile);
}
