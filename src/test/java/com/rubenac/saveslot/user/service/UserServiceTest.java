package com.rubenac.saveslot.user.service;

import com.rubenac.saveslot.exception.UserAlreadyExistsException;
import com.rubenac.saveslot.exception.UserNotFoundException;
import com.rubenac.saveslot.user.User;
import com.rubenac.saveslot.user.UserRepository;
import com.rubenac.saveslot.user.dto.UserRequest;
import com.rubenac.saveslot.user.dto.UserResponse;
import com.rubenac.saveslot.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User init() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setUsername("user");
        user.setEmail("user@gmail.com");
        user.setPassword("user123.");

        return user;
    }

    private UserResponse initToDTO(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getCreatedAt()
        );
    }

    @Test
    void getUserById_shouldReturnDTO_whenUserExists() {
        // GIVEN
        User user = init();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserResponse userResponse = initToDTO(user);

        when(userMapper.toDTO(user)).thenReturn(userResponse);

        // WHEN
        UserResponse result = userService.getUserById(user.getId());

        // THEN
        assertNotNull(result);
        assertEquals(user.getId(), result.id());
        assertEquals("user", result.username());
        assertEquals("user@gmail.com", result.email());

        verify(userRepository).findById(user.getId());
        verify(userMapper).toDTO(user);
    }

    @Test
    void getUserById_shouldThrowException_whenUserNotFound() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void updateUser_shouldReturnDTO_whenUserExists() {
        // GIVEN
        User user = init();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserRequest request = new UserRequest("updated@gmail.com", "updated");

        user.setEmail("updated@gmail.com");
        user.setUsername("updated");

        UserResponse userResponse = initToDTO(user);
        when(userMapper.toDTO(user)).thenReturn(userResponse);

        // WHEN
        UserResponse result = userService.updateUser(user.getId(), request);

        // THEN
        assertNotNull(result);
        assertEquals(user.getId(), result.id());
        assertEquals("updated@gmail.com", result.email());
        assertEquals("updated", result.username());

        verify(userRepository).findById(user.getId());
        verify(userMapper).toDTO(user);
    }

    @Test
    void updateUser_shouldThrowException_whenUserNotExists() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        UserRequest request = new UserRequest("updated@gmail.com", "updated");

        // WHEN / THEN
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, request));
        verify(userRepository).findById(userId);
    }

    @Test
    void updateUser_shouldThrowException_whenEmailAlreadyExists() {
        // GIVEN
        User user = init();
        User otherUser = new User();
        otherUser.setEmail("updated@gmail.com");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserRequest request = new UserRequest("updated@gmail.com", "updated");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // WHEN / THEN
        assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(user.getId(), request));

        verify(userRepository).findById(user.getId());
        verify(userRepository).existsByEmail(request.email());
    }

    @Test
    void updateUser_shouldThrowException_whenUsernameAlreadyExists() {
        // GIVEN
        User user = init();
        User otherUser = new User();
        otherUser.setEmail("otherUser@gmail.com");
        otherUser.setUsername("updated");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserRequest request = new UserRequest("updated@gmail.com", "updated");
        when(userRepository.existsByUsernameIgnoreCase(request.username())).thenReturn(true);

        // WHEN / THEN
        assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(user.getId(), request));

        verify(userRepository).findById(user.getId());
        verify(userRepository).existsByUsernameIgnoreCase(request.username());

    }
}
