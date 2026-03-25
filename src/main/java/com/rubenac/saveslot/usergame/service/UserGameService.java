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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class UserGameService {
    private final UserGameRepository userGameRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserGameMapper userGameMapper;

    private UserGame findUserGameByIdOrThrow(Long id) {
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

        return userGameMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public UserGameResponse getUserGameById (Long id) {
        return userGameMapper.toDTO(findUserGameByIdOrThrow(id));
    }

    public UserGameResponse updateUserGame(Long id, UserGameRequest request) {
        UserGame userGame = findUserGameByIdOrThrow(id);

        userGame.setStatus(request.status());
        userGame.setRating(request.rating());
        userGame.setReviewText(request.reviewText());
        userGame.setCompletionPercentage(request.completionPercentage());
        userGame.setCompletionDate(request.completionDate());

        return userGameMapper.toDTO(userGame);
    }

    public void deleteUserGame(Long id) {
        UserGame userGame = findUserGameByIdOrThrow(id);

        userGameRepository.delete(userGame);
    }
}
