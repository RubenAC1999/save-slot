package com.rubenac.saveslot.user.controller;

import com.rubenac.saveslot.user.dto.UserRequest;
import com.rubenac.saveslot.user.dto.UserResponse;
import com.rubenac.saveslot.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User operations")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user by Id")
    @ApiResponse(responseCode = "200", description = "Get user using id" )
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Get user by or username")
    @ApiResponse(responseCode = "200", description = "Get user using email or username")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> searchUser(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username) {
        if (email != null) {
            return ResponseEntity.ok(userService.getUserByEmail(email));
        }

        if (username != null) {
            return ResponseEntity.ok(userService.getUserByUsername(username));
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an user")
    @ApiResponse(responseCode = "200", description = "User gets updated")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "409", description = "User with same email or username already exists")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an user")
    @ApiResponse(responseCode = "204", description = "User removed")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }



}
