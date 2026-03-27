package com.rubenac.saveslot.auth.controller;

import com.rubenac.saveslot.auth.dto.AuthResponse;
import com.rubenac.saveslot.auth.dto.LoginRequest;
import com.rubenac.saveslot.auth.dto.RegisterRequest;
import com.rubenac.saveslot.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Auth operations")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register an user")
    @ApiResponse(responseCode = "201", description = "Register an user")
    @ApiResponse(responseCode = "409", description = "User with same username or email already exists")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.status(201).body(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Log in")
    @ApiResponse(responseCode = "200", description = "User log in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
