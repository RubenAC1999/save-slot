package com.rubenac.saveslot.game.dto;


import java.util.List;

public record RawgSearchResponse(
        Integer count,
        List<RawgGameSummary> results
) {}