package com.rubenac.saveslot.game.service;

import com.rubenac.saveslot.exception.GameNotFoundException;
import com.rubenac.saveslot.game.GameRepository;
import com.rubenac.saveslot.game.client.RawgClient;
import com.rubenac.saveslot.game.dto.RawgGameDetail;
import com.rubenac.saveslot.game.dto.RawgGameSummary;
import com.rubenac.saveslot.game.mapper.GameMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @Mock
    private RawgClient rawgClient;

    @InjectMocks
    private GameService gameService;


    @Test
    void searchGamesFromRawg_shouldReturnList_whenGamesFound() {
        // GIVEN
        List<RawgGameSummary> summaries = List.of(
                new RawgGameSummary(1234, "Uncharted 4", "http://image.jpg"),
                new RawgGameSummary(5678, "Uncharted 3", "http://image2.jpg")
        );

        when(rawgClient.searchGames("uncharted")).thenReturn(summaries);

        // WHEN
        List<RawgGameSummary> result = gameService.searchGamesFromRawg("uncharted");

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Uncharted 4", result.get(0).name());

        verify(rawgClient).searchGames("uncharted");
    }

    @Test
    void getGameDetailFromRawg_shouldReturnGame_WhenExists() {
        // GIVEN
        RawgGameDetail detail = new RawgGameDetail(
                1234, "Uncharted 4", "description", "http://image.jpg");

        when(rawgClient.getGameDetail(detail.id())).thenReturn(detail);

        // WHEN
        RawgGameDetail result = gameService.getGameDetailFromRawg(detail.id());

        // THEN
        assertNotNull(result);
        assertEquals(detail.id(), result.id());
        assertEquals(detail.name(), result.name());

        verify(rawgClient).getGameDetail(detail.id());
    }

    @Test
    void getGameDetailFromRawg_shouldThrowException_WhenNotExists() {
        // GIVEN
        Integer id = 9999;

        when(rawgClient.getGameDetail(id)).thenReturn(null);

        // WHEN / THEN
        assertThrows(GameNotFoundException.class, () -> gameService.getGameDetailFromRawg(id));
    }



}
