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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private User findByIdOrThrow(UUID id) {
        log.debug("Searching user with id = {}", id);
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

        if (!user.getEmail().equals(normalizedEmail)) {
            log.info("Updating user with email = {}", normalizedEmail);
            if (userRepository.existsByEmail(normalizedEmail)) {
                log.warn("Email {} already in use", normalizedEmail);
                throw new UserAlreadyExistsException("Email: " + normalizedEmail + " already in use");
            }
            user.setEmail(normalizedEmail);
            log.info("Email updated for user {}", id);
        }

        if (!user.getUsername().equalsIgnoreCase(userRequest.username())) {
            log.info("Updating user with username = {}", userRequest.username());
            if (userRepository.existsByUsernameIgnoreCase(userRequest.username())) {
                log.warn("User with username = {} already exists", userRequest.username());
                throw new UserAlreadyExistsException("Username: " + userRequest.username() + " already in use");
            }
            user.setUsername(userRequest.username());
            log.info("Username updated for user {}", id);
        }

        log.info("User updated successfully");
        return userMapper.toDTO(user);
    }

    public void deleteUser(UUID id) {
        User user = findByIdOrThrow(id);

        userRepository.delete(user);
        log.info("User with id = {} removed", id);
    }

    public void changePassword(UUID id, ChangePasswordRequest request) {
        User user = findByIdOrThrow(id);
        // TODO validar y hashear password

        user.setPassword(request.newPassword());
    }
}
