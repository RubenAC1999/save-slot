package com.rubenac.saveslot.auth.controller;

import com.rubenac.saveslot.auth.dto.AuthResponse;
import com.rubenac.saveslot.auth.dto.LoginRequest;
import com.rubenac.saveslot.auth.dto.RegisterRequest;
import com.rubenac.saveslot.auth.service.AuthService;
import com.rubenac.saveslot.auth.service.CustomUserDetailsService;
import com.rubenac.saveslot.auth.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Necesario para desactivar filtros security
public class AuthControllerTest {
    @Autowired MockMvc mockMvc;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    CustomUserDetailsService customUserDetailsService;

    // MockBean está deprecated
    @MockitoBean
    AuthService authService;

    @Test
    void register_shouldReturnToken() throws Exception {
        when(authService.register(any(RegisterRequest.class))).thenReturn(new AuthResponse("jwt"));

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON).
                        content("""
                       {"email":"test@gmail.com", "username":"test", "password":"test123456"}
                       """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt"));
    }

    @Test
    void login_shouldReturnToken() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(new AuthResponse("jwt"));

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email":"test@gmail.com", "password":"test123456"}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt"));
    }
}