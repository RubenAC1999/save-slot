package com.rubenac.saveslot.usergame.service;

import com.rubenac.saveslot.exception.GameNotFoundException;
import com.rubenac.saveslot.exception.UserGameNotFoundException;
import com.rubenac.saveslot.exception.UserNotFoundException;
import com.rubenac.saveslot.game.Game;
import com.rubenac.saveslot.game.GameRepository;
import com.rubenac.saveslot.user.User;
import com.rubenac.saveslot.user.UserRepository;
import com.rubenac.saveslot.usergame.UserGame;
import com.rubenac.saveslot.usergame.UserGameRepository;
import com.rubenac.saveslot.usergame.dto.UserGameRequest;
import com.rubenac.saveslot.usergame.dto.UserGameResponse;
import com.rubenac.saveslot.usergame.mapper.UserGameMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserGameService {
    private final UserGameRepository userGameRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserGameMapper userGameMapper;

    private UserGame findUserGameByIdOrThrow(Long id) {
        log.debug("Searching usergame with id = {}", id);
        return userGameRepository.findById(id)
                .orElseThrow(() -> new UserGameNotFoundException("UserGame with ID: " + id + " not found."));
    }

    public UserGameResponse addGameUser (UserGameRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + userId + " not found."));

        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new GameNotFoundException("Game with ID: " + request.gameId() + " not found."));

        UserGame userGame = new UserGame();
        userGame.setGame(game);
        userGame.setUser(user);
        userGame.setStatus(request.status());
        userGame.setRating(request.rating());
        userGame.setReviewText(request.reviewText());
        userGame.setCompletionPercentage(request.completionPercentage());
        userGame.setCompletionDate(request.completionDate());

        UserGame saved = userGameRepository.save(userGame);

        log.info("New usergame created with id = {}", saved.getId());
        return userGameMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<UserGameResponse> getUserGameByUserId(UUID userId) {
        return userGameRepository.findByUserId(userId).stream().map(userGameMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public UserGameResponse getUserGameById(UUID userId, Long userGameId) {
        UserGame userGame = findUserGameByIdOrThrow(userGameId);
        if (!userGame.getUser().getId().equals(userId)) {
            throw new UserGameNotFoundException("UserGame with ID: " + userGameId + " not found in User {" + userId + "}");
        }
        return userGameMapper.toDTO(userGame);
    }

    public UserGameResponse updateUserGame(UUID userId, Long userGameId, UserGameRequest request) {
        UserGame userGame = findUserGameByIdOrThrow(userGameId);

        if (!userGame.getUser().getId().equals(userId)) {
            throw new UserGameNotFoundException("UserGame with ID: " + userGameId + " not found in User {" + userId + "}");
        }

        userGame.setStatus(request.status());
        userGame.setRating(request.rating());
        userGame.setReviewText(request.reviewText());
        userGame.setCompletionPercentage(request.completionPercentage());
        userGame.setCompletionDate(request.completionDate());

        log.info("Usergame with id = {} updated successfully", userGame.getId());
        return userGameMapper.toDTO(userGame);
    }

    public void deleteUserGame(UUID userId, Long userGameId) {
        UserGame userGame = findUserGameByIdOrThrow(userGameId);

        if (!userGame.getUser().getId().equals(userId)) {
            throw new UserGameNotFoundException("UserGame with ID: " + userGameId + " not found in User {" + userId + "}");
        }

        userGameRepository.delete(userGame);
        log.info("Usergame with id = {} removed successfully", userGameId);
    }
}
