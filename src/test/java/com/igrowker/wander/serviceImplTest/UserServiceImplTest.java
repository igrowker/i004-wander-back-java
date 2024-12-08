package com.igrowker.wander.serviceImplTest;

import com.igrowker.wander.dto.user.RequestUpdateUserDto;
import com.igrowker.wander.dto.user.UserDto;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.exception.InvalidDataException;
import com.igrowker.wander.exception.ResourceNotFoundException;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private User authenticatedUser;
    private User existingUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        authenticatedUser = new User();
        authenticatedUser.setEmail("authenticated@example.com");
        authenticatedUser.setName("Authenticated User");

        existingUser = new User();
        existingUser.setId("123");
        existingUser.setEmail("test@example.com");
        existingUser.setName("Test User");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getUserProfile_WithValidId_ReturnsUserDto() {
        when(userRepository.findById("123")).thenReturn(Optional.of(existingUser));

        UserDto userDto = userService.getUserProfile("123");

        assertNotNull(userDto);
        assertEquals("Test User", userDto.getName());
        assertEquals("test@example.com", userDto.getEmail());
        verify(userRepository, times(1)).findById("123");
    }

    @Test
    void getUserProfile_WithInvalidId_ThrowsResourceNotFoundException() {
        when(userRepository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserProfile("invalid"));

        verify(userRepository, times(1)).findById("invalid");
    }

    @Test
    void getUserProfile_WithNullId_ReturnsAuthenticatedUserProfile() {
        when(userRepository.findByEmail("authenticated@example.com")).thenReturn(Optional.of(authenticatedUser));

        UserDto userDto = userService.getUserProfile(null);

        assertNotNull(userDto);
        assertEquals("Authenticated User", userDto.getName());
        assertEquals("authenticated@example.com", userDto.getEmail());
        verify(userRepository, times(1)).findByEmail("authenticated@example.com");
    }

    @Test
    void updateUserProfile_WithValidData_UpdatesUser() {
        RequestUpdateUserDto updates = new RequestUpdateUserDto();
        updates.setName("Updated Name");
        updates.setPassword("newpassword123");

        when(userRepository.findByEmail("authenticated@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpassword123")).thenReturn("encodedPassword");
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserDto updatedUser = userService.updateUserProfile(updates);

        assertNotNull(updatedUser);
        assertEquals("Updated Name", updatedUser.getName());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUserProfile_WithShortPassword_ThrowsInvalidDataException() {
        RequestUpdateUserDto updates = new RequestUpdateUserDto();
        updates.setPassword("short");

        when(userRepository.findByEmail("authenticated@example.com")).thenReturn(Optional.of(existingUser));

        assertThrows(InvalidDataException.class, () -> userService.updateUserProfile(updates));

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUserProfile_WithBlankName_ThrowsInvalidDataException() {
        RequestUpdateUserDto updates = new RequestUpdateUserDto();
        updates.setName("");

        when(userRepository.findByEmail("authenticated@example.com")).thenReturn(Optional.of(existingUser));

        assertThrows(InvalidDataException.class, () -> userService.updateUserProfile(updates));

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUserProfile_WithNonExistentEmail_ThrowsResourceNotFoundException() {
        RequestUpdateUserDto updates = new RequestUpdateUserDto();
        updates.setName("New Name");

        when(userRepository.findByEmail("authenticated@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUserProfile(updates));

        verify(userRepository, never()).save(any());
    }
}
