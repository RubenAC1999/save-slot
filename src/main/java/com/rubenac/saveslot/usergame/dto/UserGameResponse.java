package com.rubenac.saveslot.usergame.dto;

import com.rubenac.saveslot.game.dto.GameSummary;
import com.rubenac.saveslot.usergame.Status;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record UserGameResponse(
        Long id,
        UUID userId,
        GameSummary gameSummary,
        Status status,
        BigDecimal rating,
        String reviewText,
        Integer completionPercentage,
        LocalDate completionDate,
        Instant createdAt
) {}
