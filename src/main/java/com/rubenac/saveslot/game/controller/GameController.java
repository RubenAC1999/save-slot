package com.rubenac.saveslot.game.controller;

import com.rubenac.saveslot.game.dto.GameResponse;
import com.rubenac.saveslot.game.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/games")
@Tag(name = "Games", description = "Game operations")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping("/{id}")
    @Operation(summary = "Get game by Id")
    @ApiResponse(responseCode = "200", description = "Get game using id")
    @ApiResponse(responseCode = "404", description = "Game not found")
    public ResponseEntity<GameResponse> getGameById(@PathVariable UUID id) {
        return ResponseEntity.ok(gameService.getGameById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search a game")
    @ApiResponse(responseCode = "200", description = "Game found")
    @ApiResponse(responseCode = "404", description = "Game not found")
    public ResponseEntity<List<GameResponse>> searchGame(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String company) {
        if (title != null) {
            return ResponseEntity.ok(gameService.getGameByTitle(title));
        }
        if (company != null) {
            return ResponseEntity.ok(gameService.getGameByCompany(company));
        }

        return ResponseEntity.noContent().build();
    }
}
