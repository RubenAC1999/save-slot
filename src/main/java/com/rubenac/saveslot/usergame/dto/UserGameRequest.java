package com.rubenac.saveslot.usergame.dto;

import com.rubenac.saveslot.usergame.Status;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserGameRequest(
        @NotNull(message = "Game ID is required.")
        Integer gameId,

        @NotNull(message = "Status is required.")
        Status status,

        BigDecimal rating,
        String reviewText,
        Integer completionPercentage,
        LocalDate completionDate
) {
}
