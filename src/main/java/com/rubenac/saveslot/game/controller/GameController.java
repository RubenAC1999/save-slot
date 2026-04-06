package com.rubenac.saveslot.game.controller;

import com.rubenac.saveslot.game.dto.GameResponse;
import com.rubenac.saveslot.game.dto.RawgGameDetail;
import com.rubenac.saveslot.game.dto.RawgGameSummary;
import com.rubenac.saveslot.game.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/games")
@Tag(name = "Games", description = "Game operations")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping("/search")
    @Operation(summary = "Search a game")
    @ApiResponse(responseCode = "200", description = "Game found")
    @ApiResponse(responseCode = "404", description = "Game not found")
    public ResponseEntity<List<RawgGameSummary>> searchGame(@RequestParam String q) {
        return ResponseEntity.ok(gameService.searchGamesFromRawg(q));
    }

    @GetMapping("/{rawgId}")
    @Operation(summary = "Get game details")
    @ApiResponse(responseCode = "200", description = "Game found")
    @ApiResponse(responseCode = "404", description = "Game not found")
    public ResponseEntity<RawgGameDetail> getGameDetails(@PathVariable Integer rawgId) {
        return ResponseEntity.ok(gameService.getGameDetailFromRawg(rawgId));
    }
}
