package com.rubenac.saveslot.usergame.controller;


import com.rubenac.saveslot.auth.service.CustomUserDetailsService;
import com.rubenac.saveslot.auth.service.JwtService;
import com.rubenac.saveslot.exception.UserGameNotFoundException;
import com.rubenac.saveslot.game.dto.GameSummary;
import com.rubenac.saveslot.usergame.Status;
import com.rubenac.saveslot.usergame.dto.UserGameRequest;
import com.rubenac.saveslot.usergame.dto.UserGameResponse;
import com.rubenac.saveslot.usergame.service.UserGameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserGameController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserGameControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    UserGameService userGameService;

    @Test
    void getAllUserGames_shouldReturnList() throws Exception {
        UUID userId = UUID.randomUUID();
        GameSummary gameSummary = new GameSummary(UUID.randomUUID(), "game", "image.jpg");
        var userGames = List.of(
                new UserGameResponse(1L, userId, gameSummary, Status.COMPLETED, null, null, null, null, Instant.now())
        );

        when(userGameService.getUserGameByUserId(userId)).thenReturn(userGames);

        mockMvc.perform(get("/api/v1/users/{userId}/library", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getUserGame_shouldReturnDTO_whenExists() throws Exception {
        UUID userId = UUID.randomUUID();
        GameSummary gameSummary = new GameSummary(UUID.randomUUID(), "game", "image.jpg");

        when(userGameService.getUserGameById(userId, 1L)).
                thenReturn(new UserGameResponse(1L, userId, gameSummary, Status.COMPLETED, null, null, null, null, Instant.now())
                );

        mockMvc.perform(get("/api/v1/users/{userId}/library/{userGameId}", userId, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(userId.toString()));
    }

    @Test
    void getUserGame_shouldThrowException_whenNotExists() throws Exception {
        UUID userId = UUID.randomUUID();

        when(userGameService.getUserGameById(userId, 1L))
                .thenThrow(new UserGameNotFoundException("UserGame not found"));

        mockMvc.perform(get("/api/v1/users/{userId}/library/{userGameId}", userId, 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserGame_shouldReturnDTO_whenDataIsValid() throws Exception {
        UUID userId = UUID.randomUUID();

        GameSummary gameSummary = new GameSummary(UUID.randomUUID(), "game", "image.jpg");
        UserGameRequest userGameRequest = new UserGameRequest(1, Status.COMPLETED, null, null, null, null);

        when(userGameService.updateUserGame(userId, 1L, userGameRequest))
                .thenReturn(new UserGameResponse(1L, userId, gameSummary, Status.COMPLETED, null, null, null, null, Instant.now())
                );

        mockMvc.perform(put("/api/v1/users/{userId}/library/{userGameId}", userId, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"gameId":1, "status":"COMPLETED"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteUserGame_whenExists() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{userId}/library/{userGameId}", UUID.randomUUID(), 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void addUserGame_shouldReturnDTO_whenDataIsValid() throws Exception {
        UUID userId = UUID.randomUUID();

        GameSummary gameSummary = new GameSummary(UUID.randomUUID(), "game", "image.jpg");
        UserGameRequest userGameRequest = new UserGameRequest(1, Status.COMPLETED, null, null, null, null);

        when(userGameService.addGameUser(userGameRequest, userId))
                .thenReturn(new UserGameResponse(1L, userId, gameSummary, Status.COMPLETED, null, null, null, null, Instant.now())
                );

        mockMvc.perform(post("/api/v1/users/{userId}/library", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"gameId":1, "status":"COMPLETED"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.id").value(1L));
    }
}
