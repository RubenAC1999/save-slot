package com.rubenac.saveslot.auth.service;

import com.rubenac.saveslot.auth.dto.AuthResponse;
import com.rubenac.saveslot.auth.dto.LoginRequest;
import com.rubenac.saveslot.auth.dto.RegisterRequest;
import com.rubenac.saveslot.exception.UserAlreadyExistsException;
import com.rubenac.saveslot.user.User;
import com.rubenac.saveslot.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = request.email().toLowerCase();

        log.info("Registration attempt with {}", normalizedEmail);
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException("User already exists with email: " + normalizedEmail);
        }

        if (userRepository.existsByUsernameIgnoreCase(request.username())) {
            throw new UserAlreadyExistsException("User already exists with username: " + request.username());

        }

        User user = new User();
        user.setEmail(normalizedEmail);
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));

        User saved = userRepository.save(user);

        String token = jwtService.generateToken(saved);

        log.info("Registration successfull for {}", saved.getEmail());

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = request.email().toLowerCase();

        log.info("Login attempt with {}", normalizedEmail);

        Authentication authToken = new UsernamePasswordAuthenticationToken(
                normalizedEmail,
                request.password()
        );

        authenticationManager.authenticate(authToken);

       UserDetails userDetails =  userDetailsService.loadUserByUsername(normalizedEmail);
       String token = jwtService.generateToken(userDetails);

       log.info("Login successfull with {}", normalizedEmail);

       return new AuthResponse(token);
    }
}
