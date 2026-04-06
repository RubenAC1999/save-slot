package com.rubenac.saveslot.game.service;

import com.rubenac.saveslot.exception.GameNotFoundException;
import com.rubenac.saveslot.game.Game;
import com.rubenac.saveslot.game.GameRepository;
import com.rubenac.saveslot.game.client.RawgClient;
import com.rubenac.saveslot.game.dto.GameResponse;
import com.rubenac.saveslot.game.dto.RawgGameDetail;
import com.rubenac.saveslot.game.dto.RawgGameSummary;
import com.rubenac.saveslot.game.mapper.GameMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final RawgClient rawgClient;

    private Game findByIdOrThrow(UUID id) {
        log.debug("Searching game with id = {}", id);
        return gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Game with ID: " + id + " not found."));
    }

    @Transactional(readOnly = true)
    public GameResponse getGameById(UUID id) {
        return gameMapper.toDTO(findByIdOrThrow(id));
    }

    @Transactional(readOnly = true)
    public Page<GameResponse> getGameByTitle(String title, Pageable pageable) {
        return gameRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(gameMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<GameResponse> getGameByCompany(String company, Pageable pageable) {
        return gameRepository.findByCompanyContainingIgnoreCase(company, pageable)
                .map(gameMapper::toDTO);
    }

    public List<RawgGameSummary> searchGamesFromRawg(String query) {
            return rawgClient.searchGames(query);
    }

    public RawgGameDetail getGameDetailFromRawg(Integer id) {
        RawgGameDetail game = rawgClient.getGameDetail(id);

        if (game == null) {
            throw new GameNotFoundException("Game with ID: " + id + " not found.");
        }

        return game;
    }
}
