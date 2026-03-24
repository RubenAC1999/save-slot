package com.rubenac.saveslot.game.dto;

import java.util.UUID;

public record GameSummary(
        UUID id,
        String title,
        String coverImageUrl
) {}
