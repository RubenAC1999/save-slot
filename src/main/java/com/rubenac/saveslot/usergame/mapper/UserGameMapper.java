package com.rubenac.saveslot.usergame.mapper;

import com.rubenac.saveslot.usergame.UserGame;
import com.rubenac.saveslot.usergame.dto.UserGameResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserGameMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "game.id", target = "gameSummary.id")
    @Mapping(source = "game.title", target = "gameSummary.title")
    @Mapping(source = "game.coverImageUrl", target = "gameSummary.coverImageUrl")
    UserGameResponse toDTO(UserGame userGame);
}
