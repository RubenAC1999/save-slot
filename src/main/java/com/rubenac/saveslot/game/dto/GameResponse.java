package com.rubenac.saveslot.game.dto;

import java.time.LocalDate;
import java.util.UUID;

public record GameResponse(
        UUID id,
        Integer rawgId,
        String title,
        String coverImageUrl,
        String synopsis,
        String company,
        LocalDate releaseDate
) {}
