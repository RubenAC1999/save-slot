package com.rubenac.saveslot.user.controller;

import com.rubenac.saveslot.auth.service.CustomUserDetailsService;
import com.rubenac.saveslot.auth.service.JwtService;
import com.rubenac.saveslot.exception.UserAlreadyExistsException;
import com.rubenac.saveslot.user.dto.UserRequest;
import com.rubenac.saveslot.user.dto.UserResponse;
import com.rubenac.saveslot.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    UserService userService;

    @Test
    void getUserById_shouldReturnDTO() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userService.getUserById(userId))
                .thenReturn(new UserResponse(userId, "test@gmail.com", "test", Instant.now()));

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.username").value("test"));
    }

    @Test
    void searchUserByEmail_shouldReturnDTO_whenExists() throws Exception{
        when(userService.getUserByEmail("test@gmail.com"))
                .thenReturn(new UserResponse(UUID.randomUUID(), "test@gmail.com", "test", Instant.now()));


        mockMvc.perform(get("/api/v1/users/search")
                .param("email", "test@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.username").value("test"));
    }

    @Test
    void searchUserByUsername_shouldReturnDTO_whenExists() throws Exception{
        when(userService.getUserByUsername("test"))
                .thenReturn(new UserResponse(UUID.randomUUID(), "test@gmail.com", "test", Instant.now()));


        mockMvc.perform(get("/api/v1/users/search")
                        .param("username", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.username").value("test"));
    }

    @Test
    void searchUser_shouldReturnException_whenNotExists() throws Exception {
        mockMvc.perform(get("/api/v1/users/search"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_shouldReturnDTO_whenDataIsValid() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userService.updateUser(userId, new UserRequest("updated@gmail.com", "updated")))
                .thenReturn(new UserResponse(UUID.randomUUID(), "updated@gmail.com", "updated", Instant.now()));

        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"updated@gmail.com", "username":"updated"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@gmail.com"))
                .andExpect(jsonPath("$.username").value("updated"));
    }

    @Test
    void updateUser_shouldReturnException_whenIsConflict() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userService.updateUser(userId, new UserRequest("updated@gmail.com", "updated")))
                .thenThrow(new UserAlreadyExistsException("Email already in use"));

        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"updated@gmail.com", "username":"updated"}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteUser_whenUserExists() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isNoContent());
    }
}
