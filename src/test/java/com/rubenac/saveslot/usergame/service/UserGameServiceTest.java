package com.rubenac.saveslot.usergame.service;

import com.rubenac.saveslot.exception.GameNotFoundException;
import com.rubenac.saveslot.exception.UserGameNotFoundException;
import com.rubenac.saveslot.exception.UserNotFoundException;
import com.rubenac.saveslot.game.Game;
import com.rubenac.saveslot.game.GameRepository;
import com.rubenac.saveslot.game.client.RawgClient;
import com.rubenac.saveslot.game.dto.GameSummary;
import com.rubenac.saveslot.user.User;
import com.rubenac.saveslot.user.UserRepository;
import com.rubenac.saveslot.usergame.Status;
import com.rubenac.saveslot.usergame.UserGame;
import com.rubenac.saveslot.usergame.UserGameRepository;
import com.rubenac.saveslot.usergame.dto.UserGameRequest;
import com.rubenac.saveslot.usergame.dto.UserGameResponse;
import com.rubenac.saveslot.usergame.mapper.UserGameMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserGameServiceTest {
    @Mock
    private UserGameRepository userGameRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserGameMapper userGameMapper;

    @Mock
    private RawgClient rawgClient;

    @InjectMocks
    private UserGameService userGameService;

    private User initUser() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setUsername("user");
        user.setEmail("user@gmail.com");
        user.setPassword("user123.");

        return user;
    }

    private UserGame initUserGame(User user, Game game) {
        UserGame userGame = new UserGame();
        userGame.setId(1L);
        userGame.setUser(user);
        userGame.setGame(game);
        userGame.setStatus(Status.PLAYING);
        return userGame;
    }

    private Game initGame() {
        Game game = new Game();
        game.setId(UUID.randomUUID());
        game.setRawgId(1234);
        game.setTitle("Uncharted 4");
        game.setCoverImageUrl("http://image.jpg");
        return game;
    }

    @Test
    void getUserGameByUserId_shouldReturnList() {
        // GIVEN
        User user = initUser();
        Game game = initGame();

        UserGame userGame = initUserGame(user, game);

        GameSummary gameSummary = new GameSummary(UUID.randomUUID(), game.getTitle(), game.getCoverImageUrl());
        UserGameResponse userGameResponse = new UserGameResponse(
                userGame.getId(), user.getId(), gameSummary, Status.COMPLETED, null, null,
                null, null, Instant.now());

        when(userGameRepository.findByUserId(user.getId())).thenReturn(List.of(userGame));
        when(userGameMapper.toDTO(userGame)).thenReturn(userGameResponse);

        List<UserGameResponse> result = userGameService.getUserGameByUserId(user.getId());

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Uncharted 4", result.get(0).gameSummary().title());

        verify(userGameRepository).findByUserId(user.getId());
        verify(userGameMapper).toDTO(userGame);
    }

    @Test
    void getUserGameById_shouldReturnDTO_whenExists() {
        // GIVEN
        User user = initUser();
        Game game = initGame();
        UserGame userGame = initUserGame(user, game);

        when(userGameRepository.findById(userGame.getId())).thenReturn(Optional.of(userGame));

        GameSummary gameSummary = new GameSummary(UUID.randomUUID(), game.getTitle(), game.getCoverImageUrl());
        UserGameResponse userGameResponse = new UserGameResponse(
                userGame.getId(), user.getId(), gameSummary, Status.COMPLETED, null, null,
                null, null, Instant.now());

        when(userGameMapper.toDTO(userGame)).thenReturn(userGameResponse);

        // WHEN
        UserGameResponse result = userGameService.getUserGameById(user.getId(), userGame.getId());

        // THEN
        assertNotNull(result);
        assertEquals(result.id(), userGame.getId());
        assertEquals(result.userId(), userGame.getUser().getId());

        verify(userGameMapper).toDTO(userGame);
    }

    @Test
    void getUserGameByUserId_shouldThrowException_whenUserGameNotExists() {
        // GIVEN
        User user = initUser();

        when(userGameRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(UserGameNotFoundException.class, () -> userGameService.getUserGameById(user.getId(), 1L));
    }

    @Test
    void updateGameUser_shouldReturnDTO_whenUsergameExists() {
        //GIVEN
        User user = initUser();
        Game game = initGame();
        UserGame userGame = initUserGame(user, game);
        UserGameRequest request = new UserGameRequest(
                game.getRawgId(), Status.COMPLETED, null, null, null, null);

        when(userGameRepository.findById(userGame.getId())).thenReturn(Optional.of(userGame));

        userGame.setStatus(request.status());
        userGame.setRating(request.rating());
        userGame.setReviewText(request.reviewText());
        userGame.setCompletionPercentage(request.completionPercentage());
        userGame.setCompletionDate(request.completionDate());

        GameSummary gameSummary = new GameSummary(UUID.randomUUID(), game.getTitle(), game.getCoverImageUrl());
        UserGameResponse userGameResponse = new UserGameResponse(
                userGame.getId(), user.getId(), gameSummary, Status.COMPLETED, null, null,
                null, null, Instant.now());


        when(userGameMapper.toDTO(userGame)).thenReturn(userGameResponse);

        // WHEN
        UserGameResponse result = userGameService.updateUserGame(user.getId(), userGame.getId(), request);

        // THEN
        assertNotNull(result);
        assertEquals(result.userId(), userGame.getUser().getId());
        assertEquals(result.id(), userGame.getId());
        assertEquals(result.status(), userGame.getStatus());

        verify(userGameRepository).findById(userGame.getId());
        verify(userGameMapper).toDTO(userGame);
    }

    @Test
    void updateUserGame_shouldThrowException_whenUserGameNotExists() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        Long userGameId = 1L;
        Integer gameId = 1;
        UserGameRequest request = new UserGameRequest(
               gameId, Status.COMPLETED, null, null, null, null);

        when(userGameRepository.findById(userGameId)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(UserGameNotFoundException.class, () -> userGameService.updateUserGame(userId, userGameId, request));

        verify(userGameRepository).findById(userGameId);
    }

    @Test
    void deleteUserGame_shouldDelete_ifUserGameExists() {
        // GIVEN
        User user = initUser();
        Game game = initGame();
        UserGame userGame = initUserGame(user, game);

        when(userGameRepository.findById(userGame.getId())).thenReturn(Optional.of(userGame));

        // WHEN
        userGameService.deleteUserGame(user.getId(), userGame.getId());

        // THEN
        verify(userGameRepository).delete(userGame);
    }

    @Test
    void addGameUser_shouldReturnDTO_whenAllExists() {
        // GIVEN
        User user = initUser();
        Game game = initGame();
        UserGame userGame = initUserGame(user, game);
        UserGameRequest request = new UserGameRequest(
                game.getRawgId(), Status.COMPLETED, null, null, null, null);

        when(gameRepository.findByRawgId(game.getRawgId())).thenReturn(Optional.of(game));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userGame.setGame(game);
        userGame.setUser(user);
        userGame.setStatus(request.status());
        userGame.setRating(request.rating());
        userGame.setReviewText(request.reviewText());
        userGame.setCompletionPercentage(request.completionPercentage());
        userGame.setCompletionDate(request.completionDate());


        when(userGameRepository.save(any(UserGame.class))).thenReturn(userGame);

        GameSummary gameSummary = new GameSummary(UUID.randomUUID(), game.getTitle(), game.getCoverImageUrl());
        UserGameResponse userGameResponse = new UserGameResponse(
                userGame.getId(), user.getId(), gameSummary, Status.COMPLETED, null, null,
                null, null, Instant.now());

        when(userGameMapper.toDTO(userGame)).thenReturn(userGameResponse);

        // WHEN
        UserGameResponse result = userGameService.addGameUser(request, user.getId());

        // THEN
        assertNotNull(result);
        assertEquals(result.userId(), userGame.getUser().getId());
        assertEquals(result.gameSummary().title(), userGame.getGame().getTitle());

        verify(userRepository).findById(user.getId());
        verify(gameRepository).findByRawgId(game.getRawgId());
        verify(userGameRepository).save(any(UserGame.class));
        verify(userGameMapper).toDTO(userGame);
    }

    @Test
    void addGameUser_shouldThrowException_WhenUserNotExists() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        Game game = initGame();
        UserGameRequest request = new UserGameRequest(
                game.getRawgId(), Status.COMPLETED, null, null, null, null);


        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(UserNotFoundException.class, () -> userGameService.addGameUser(request, userId));

        verify(userRepository).findById(userId);
    }

    @Test
    void addGameUser_shouldThrowException_WhenGameNotExists() {
        // GIVEN
        User user = initUser();
        Integer gameId = 1;
        UserGameRequest request = new UserGameRequest(
              gameId, Status.COMPLETED, null, null, null, null);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(gameRepository.findByRawgId(gameId)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(GameNotFoundException.class, () -> userGameService.addGameUser(request, user.getId()));

        verify(userRepository).findById(user.getId());
        verify(gameRepository).findByRawgId(gameId);
    }

}
