package com.rubenac.saveslot.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RawgGameDetail(
        Integer id,
        String name,
        String description,

        @JsonProperty("background_image")
        String backgroundImage
) {
}
