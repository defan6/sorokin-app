package com.ddos.profile.mapper;

import com.ddos.profile.dto.CreateProfileRequest;
import com.ddos.profile.dto.ProfileResponse;
import com.ddos.profile.dto.UpdateProfileRequest;
import com.ddos.profile.event.UserRegisteredEvent;
import com.ddos.profile.model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mappings({
            @Mapping(target = "userId", source = "userId"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "fullName", source = "fullName"),
    })
    CreateProfileRequest toProfileRequest(UserRegisteredEvent event);


    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateProfileFromRequest(UpdateProfileRequest request, @MappingTarget Profile profile);

    ProfileResponse toResponse(Profile profile);

    Profile toProfile(CreateProfileRequest createProfileRequest);
}
