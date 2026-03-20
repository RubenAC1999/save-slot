package com.rubenac.saveslot.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
    @NotBlank(message = "Email is required")
    @Email
    String email,

    @NotBlank(message = "Username is required")
    String username,

    @NotBlank(message = "Password is required")
    String password
)
{}
