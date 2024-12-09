package com.igrowker.wander.controllerTest;


import com.igrowker.wander.controller.AuthController;
import com.igrowker.wander.dto.user.*;
import com.igrowker.wander.security.JwtService;
import com.igrowker.wander.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerTest {
    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testRegisterUser() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto(); // Configurar los datos necesarios
        ResponseUserDto responseUserDto = new ResponseUserDto(); // Configurar datos simulados
        when(authService.registerUser(any(RegisterUserDto.class))).thenReturn(responseUserDto);

        mockMvc.perform(post("/api/autenticacion/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testUser\", \"email\": \"test@test.com\", \"password\": \"password\"}"))
                .andExpect(status().isCreated());

        verify(authService, times(1)).registerUser(any(RegisterUserDto.class));
    }

    @Test
    void testLogin() throws Exception {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken("token123");

        when(authService.authenticateUser(any(LoginRequest.class))).thenReturn(loginResponse);

        // Realizar la solicitud simulada y verificar el resultado
        mockMvc.perform(post("/api/autenticacion/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@test.com\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token123"));

        verify(authService, times(1)).authenticateUser(any(LoginRequest.class));
    }

    @Test
    void testVerifyUser() throws Exception {
        RequestVerifyUserDto verifyUserDto = new RequestVerifyUserDto();
        ResponseUserDto responseUserDto = new ResponseUserDto();
        when(authService.verifyUser(any(RequestVerifyUserDto.class))).thenReturn(responseUserDto);

        mockMvc.perform(post("/api/autenticacion/verify-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@test.com\", \"code\": \"12345\"}"))
                .andExpect(status().isOk());

        verify(authService, times(1)).verifyUser(any(RequestVerifyUserDto.class));
    }

    @Test
    void testLogout() throws Exception {
        when(authService.logout(anyString())).thenReturn("Logged out");

        mockMvc.perform(post("/api/autenticacion/logout")
                        .header("Authorization", "Bearer token123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out"));

        verify(authService, times(1)).logout(anyString());
    }

    @Test
    void testForgotPassword() throws Exception {
        doNothing().when(authService).sendForgotPasswordEmail(anyString());

        mockMvc.perform(post("/api/autenticacion/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@test.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Correo enviado si el email existe en nuestro sistema"));

        verify(authService, times(1)).sendForgotPasswordEmail(anyString());
    }

    @Test
    void testResetPassword() throws Exception {
        doNothing().when(authService).resetPassword(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/autenticacion/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@test.com\", \"code\": \"12345\", \"newPassword\": \"newPass123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Contraseña restablecida con éxito"));

        verify(authService, times(1)).resetPassword(anyString(), anyString(), anyString());
    }

    @Test
    void testResendVerificationCode() throws Exception {
        doNothing().when(authService).resendVerificationCode(anyString());

        mockMvc.perform(post("/api/autenticacion/reenviar-codigo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test@test.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Nuevo código de verificación enviado al correo proporcionado."));

        verify(authService, times(1)).resendVerificationCode(anyString());
    }
}
