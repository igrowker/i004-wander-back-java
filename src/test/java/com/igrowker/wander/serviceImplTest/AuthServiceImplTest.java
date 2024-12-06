package com.igrowker.wander.serviceImplTest;

import com.igrowker.wander.dto.user.*;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.exception.InvalidUserCredentialsException;
import com.igrowker.wander.exception.ResourceAlreadyExistsException;
import com.igrowker.wander.exception.ResourceNotFoundException;
import com.igrowker.wander.repository.RevokedTokenRepository;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.security.JwtService;
import com.igrowker.wander.service.EmailService;
import com.igrowker.wander.serviceimpl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RevokedTokenRepository revokedTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testRegisterUser_Success() {
        // Arrange
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        userDto.setPassword("password123");
        userDto.setRole("USER");
        userDto.setLocation("USA");

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encoded_password");

        User savedUser = new User();
        savedUser.setId("1");
        savedUser.setName(userDto.getName());
        savedUser.setEmail(userDto.getEmail());
        savedUser.setRole(userDto.getRole());
        savedUser.setLocation(userDto.getLocation());
        savedUser.setCreatedAt(new Date());  // Reemplazo de LocalDateTime con Date

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        ResponseUserDto response = authService.registerUser(userDto);

        // Assert
        assertNotNull(response);
        assertEquals(savedUser.getId(), response.getId());
        assertEquals(savedUser.getName(), response.getName());
        assertEquals(savedUser.getEmail(), response.getEmail());
        assertEquals(savedUser.getRole(), response.getRole());
        verify(emailService, times(1)).sendVerificationEmail(any(User.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setEmail("john@example.com");

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> authService.registerUser(userDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testVerifyUser_Success() {
        // Arrange
        RequestVerifyUserDto verifyUserDto = new RequestVerifyUserDto();
        verifyUserDto.setEmail("john@example.com");
        verifyUserDto.setVerificationCode("123456");

        User user = new User();
        user.setEmail("john@example.com");
        user.setVerificationCode("123456");
        user.setVerificationCodeExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000));  // Reemplazo de LocalDateTime con Date

        when(userRepository.findByEmail(verifyUserDto.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        ResponseUserDto response = authService.verifyUser(verifyUserDto);

        // Assert
        assertNotNull(response);
        assertEquals("john@example.com", response.getEmail());
        assertNull(user.getVerificationCode());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testVerifyUser_UserNotFound() {
        // Arrange
        RequestVerifyUserDto verifyUserDto = new RequestVerifyUserDto();
        verifyUserDto.setEmail("nonexistent@example.com");

        when(userRepository.findByEmail(verifyUserDto.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> authService.verifyUser(verifyUserDto));
    }

    @Test
    void testAuthenticateUser_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("password123");

        User user = new User();
        user.setEmail("john@example.com");
        user.setEnabled(true);

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("test_jwt_token");

        // Act
        LoginResponse response = authService.authenticateUser(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("test_jwt_token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testAuthenticateUser_UserNotEnabled() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");

        User user = new User();
        user.setEnabled(false);

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(InvalidUserCredentialsException.class, () -> authService.authenticateUser(loginRequest));
    }
}
