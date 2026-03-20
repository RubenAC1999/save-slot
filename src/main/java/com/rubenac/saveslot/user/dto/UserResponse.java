package com.rubenac.saveslot.user.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
        UUID uuid,
        String email,
        String username,
        Instant createdAt
)
{}
