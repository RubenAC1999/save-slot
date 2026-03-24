package com.rubenac.saveslot.game.service;

import com.rubenac.saveslot.exception.GameNotFoundException;
import com.rubenac.saveslot.game.Game;
import com.rubenac.saveslot.game.GameRepository;
import com.rubenac.saveslot.game.dto.GameResponse;
import com.rubenac.saveslot.game.mapper.GameMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    private Game findByIdOrThrow(UUID id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Game with ID: " + id + " not found."));
    }

    @Transactional(readOnly = true)
    public GameResponse findGameById(UUID id) {
        return gameMapper.toDTO(findByIdOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<GameResponse> findGameByTitle(String title) {
        List<Game> games = gameRepository.findByTitleContainingIgnoreCase(title);

        return games.stream().map(gameMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<GameResponse> findGameByCompany(String company) {
        List<Game> games = gameRepository.findByCompanyContainingIgnoreCase(company);

        return games.stream().map(gameMapper::toDTO).toList();
    }
}
