package com.rubenac.saveslot.usergame.controller;

import com.rubenac.saveslot.usergame.dto.UserGameRequest;
import com.rubenac.saveslot.usergame.dto.UserGameResponse;
import com.rubenac.saveslot.usergame.service.UserGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/library")
@Tag(name = "UserGames", description = "UserGame operations")
@RequiredArgsConstructor
public class UserGameController {
    private final UserGameService userGameService;


    @PostMapping
    @Operation(summary = "Add an usergame")
    @ApiResponse(responseCode = "201", description = "UserGame created successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserGameResponse> addUserGame(@PathVariable UUID userId, @RequestBody @Valid UserGameRequest request) {
        return ResponseEntity.status(201).body(userGameService.addGameUser(request, userId));
    }

    @GetMapping
    @Operation(summary = "Get all games from user's library")
    @ApiResponse(responseCode = "200", description = "Show all games from library")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<List<UserGameResponse>> getAllUserGames(@PathVariable UUID userId) {
        return ResponseEntity.ok(userGameService.getUserGameByUserId(userId));
    }

    @GetMapping("/{userGameId}")
    @Operation(summary = "Get a game from the library")
    @ApiResponse(responseCode = "200", description = "Get game from library")
    @ApiResponse(responseCode = "404", description = "User or game not found")
    public ResponseEntity<UserGameResponse> getUserGame(@PathVariable UUID userId, @PathVariable Long userGameId) {
        return ResponseEntity.ok(userGameService.getUserGameById(userId, userGameId));
    }

    @PutMapping("/{userGameId}")
    @Operation(summary = "Update a game user")
    @ApiResponse(responseCode = "200", description = "Game from library updated")
    @ApiResponse(responseCode = "404", description = "User or userGame not found")
    public ResponseEntity<UserGameResponse> updateUserGame(@PathVariable UUID userId, @PathVariable Long userGameId,
                                                           @RequestBody @Valid UserGameRequest request) {
        return ResponseEntity.ok(userGameService.updateUserGame(userId, userGameId, request));
    }

    @DeleteMapping("/{userGameId}")
    @Operation(summary = "Remove a game from library")
    @ApiResponse(responseCode = "204", description = "Game removed from library")
    @ApiResponse(responseCode = "404", description = "User or userGame not found")
    public ResponseEntity<Void> deleteUserGame(@PathVariable UUID userId, @PathVariable Long userGameId) {
        userGameService.deleteUserGame(userId, userGameId);
        return ResponseEntity.noContent().build();
    }

}
