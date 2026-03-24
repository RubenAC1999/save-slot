package com.rubenac.saveslot.game.mapper;

import com.rubenac.saveslot.game.Game;
import com.rubenac.saveslot.game.dto.GameResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameMapper {
    GameResponse toDTO(Game game);
}
