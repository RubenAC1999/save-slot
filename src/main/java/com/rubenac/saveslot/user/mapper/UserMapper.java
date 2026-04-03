package com.rubenac.saveslot.user.mapper;

import com.rubenac.saveslot.user.User;
import com.rubenac.saveslot.user.dto.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDTO(User user);
}
