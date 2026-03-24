package com.rubenac.saveslot.usergame.mapper;

import com.rubenac.saveslot.usergame.UserGame;
import com.rubenac.saveslot.usergame.dto.UserGameRequest;
import com.rubenac.saveslot.usergame.dto.UserGameResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserGameMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "game.id", target = "gameSummary.id")
    UserGameResponse toDTO(UserGame usergame);
    UserGame toEntity(UserGameRequest request);
}
