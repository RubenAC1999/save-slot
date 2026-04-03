package com.rubenac.saveslot.user.mapper;

import com.rubenac.saveslot.user.User;
import com.rubenac.saveslot.user.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "userHandle", target = "username")
    UserResponse toDTO(User user);
}
