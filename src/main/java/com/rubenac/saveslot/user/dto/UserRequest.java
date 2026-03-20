package com.rubenac.saveslot.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
    @NotBlank(message = "Email is required")
    @Email
    String email,

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30)
    String username,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72)  // BCrypt acorta las passwords a 72 caracteres.
    String password
)
{}
