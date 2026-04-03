package com.rubenac.saveslot.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RawgGameSummary(
    Integer id,
    String name,

    @JsonProperty("background_image")
    String backgroundImage
    )
{}
