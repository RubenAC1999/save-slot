package com.rubenac.saveslot.user.service;

import com.rubenac.saveslot.exception.UserAlreadyExistsException;
import com.rubenac.saveslot.exception.UserNotFoundException;
import com.rubenac.saveslot.user.User;
import com.rubenac.saveslot.user.UserRepository;
import com.rubenac.saveslot.user.dto.ChangePasswordRequest;
import com.rubenac.saveslot.user.dto.UserRequest;
import com.rubenac.saveslot.user.dto.UserResponse;
import com.rubenac.saveslot.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private User findByIdOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + id + " not found."));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        User user = findByIdOrThrow(id);

        return userMapper.toDTO(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        String normalizedEmail = email.toLowerCase();
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + normalizedEmail + " not found."));

        return userMapper.toDTO(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found."));

        return userMapper.toDTO(user);
    }

    public UserResponse updateUser(UUID id, UserRequest userRequest) {
        User user = findByIdOrThrow(id);

        String normalizedEmail = userRequest.email().toLowerCase();
        String normalizedUsername = userRequest.username().toLowerCase();

        if (!user.getEmail().equals(normalizedEmail)) {
            if (userRepository.existsByEmail(normalizedEmail)) {
                throw new UserAlreadyExistsException("Email: " + normalizedEmail + " already in use");
            }
            user.setEmail(normalizedEmail);
        }

        if (!user.getUsername().equals(normalizedUsername)) {
            if (userRepository.existsByUsername(normalizedUsername)) {
                throw new UserAlreadyExistsException("Username: " + normalizedUsername + " already in use");
            }
            user.setUsername(normalizedUsername);
        }

        return userMapper.toDTO(user);
    }

    public void removeUser(UUID id) {
        User user = findByIdOrThrow(id);

        userRepository.delete(user);
    }

    public void changePassword(UUID id, ChangePasswordRequest request) {
        User user = findByIdOrThrow(id);
        // TODO validar y hashear password

        user.setPassword(request.newPassword());
    }
}
