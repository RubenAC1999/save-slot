package com.rubenac.saveslot.game.controller;

import com.rubenac.saveslot.auth.service.CustomUserDetailsService;
import com.rubenac.saveslot.auth.service.JwtService;
import com.rubenac.saveslot.exception.GameNotFoundException;
import com.rubenac.saveslot.game.dto.RawgGameDetail;
import com.rubenac.saveslot.game.dto.RawgGameSummary;
import com.rubenac.saveslot.game.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = GameController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GameControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    CustomUserDetailsService customUserDetailsService;

    // MockBean está deprecated
    @MockitoBean
    GameService gameService;

    @Test
    void searchGame_shouldReturnList_whenGameExists() throws Exception {
        var summaries = List.of(
                new RawgGameSummary(1234, "Uncharted 4", "http://image.jpg")
        );

        when(gameService.searchGamesFromRawg("uncharted")).thenReturn(summaries);

        mockMvc.perform(get("/api/v1/games/search")
                        .param("q", "uncharted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Uncharted 4"));
    }

    @Test
    void getGameDetails_shouldReturnDTO_whenGameExists() throws Exception {
        Integer rawgId = 1;

        when(gameService.getGameDetailFromRawg(rawgId))
                .thenReturn(new RawgGameDetail(1, "uncharted", "description", "image.jpg"));

        mockMvc.perform(get("/api/v1/games/{rawgId}", rawgId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rawgId))
                .andExpect(jsonPath("$.name").value("uncharted"));
    }

    @Test
    void getGameDetails_shouldThrowException_whenGameNotExists() throws Exception {
        Integer rawgId = 1;

        when(gameService.getGameDetailFromRawg(rawgId))
                .thenThrow(new GameNotFoundException("Game not found"));

        mockMvc.perform(get("/api/v1/games/{rawgId}", rawgId))
                .andExpect(status().isNotFound());
    }
}
