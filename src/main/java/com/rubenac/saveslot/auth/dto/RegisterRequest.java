package com.rubenac.saveslot.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Email is required")
        @Email
        String email,
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 30)
        String username,
        @NotBlank(message = "Password is required")
        @Size(min = 9, max = 72)
        String password
) {
}
