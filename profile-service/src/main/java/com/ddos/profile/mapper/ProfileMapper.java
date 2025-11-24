package com.ddos.profile.mapper;

import com.ddos.profile.event.UserRegisteredEvent;
import com.ddos.profile.model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mappings({
            @Mapping(target = "userId", source = "userId"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "fullName", source = "fullName"),
            @Mapping(target = "bio", ignore = true),
            @Mapping(target = "avatarUrl", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    Profile toProfile(UserRegisteredEvent event);
}
