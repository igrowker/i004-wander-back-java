package com.igrowker.wander.serviceImplTest;

import com.igrowker.wander.entity.User;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.serviceimpl.UploadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UploadServiceImplTest {
    @InjectMocks
    private UploadServiceImpl uploadService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void setAvatar_ShouldUpdateAvatarWhenUserExists() {
        // Arrange
        String userId = "123";
        String imageUrl = "http://example.com/avatar.png";

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setAvatar("http://old-avatar.com/avatar.png");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        uploadService.setAvatar(imageUrl, userId);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(imageUrl, savedUser.getAvatar(), "Avatar URL should be updated");
    }

    @Test
    void setAvatar_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String userId = "123";
        String imageUrl = "http://example.com/avatar.png";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> uploadService.setAvatar(imageUrl, userId),
                "Should throw exception when user is not found");
    }
}
