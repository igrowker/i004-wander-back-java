package com.igrowker.wander.entityTest;

import com.igrowker.wander.entity.Notification;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationTest {

    @Test
    void testNotificationDefaultConstructor() {
        // Act
        Notification notification = new Notification();

        // Assert
        assertNull(notification.getId());
        assertNull(notification.getUserId());
        assertNull(notification.getType());
        assertNull(notification.getMessage());
        assertFalse(notification.isRead());
        assertNotNull(notification.getCreatedAt());
    }

    @Test
    void testNotificationAllArgsConstructor() {
        // Arrange
        String id = "1";
        String userId = "user123";
        String type = "INFO";
        String message = "Test notification";
        boolean isRead = true;
        LocalDateTime createdAt = LocalDateTime.of(2023, 12, 1, 10, 0);

        // Act
        Notification notification = new Notification(id, userId, type, message, isRead, createdAt);

        // Assert
        assertEquals(id, notification.getId());
        assertEquals(userId, notification.getUserId());
        assertEquals(type, notification.getType());
        assertEquals(message, notification.getMessage());
        assertTrue(notification.isRead());
        assertEquals(createdAt, notification.getCreatedAt());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        Notification notification = new Notification();
        String id = "2";
        String userId = "user456";
        String type = "WARNING";
        String message = "Warning notification";
        boolean isRead = true;
        LocalDateTime createdAt = LocalDateTime.of(2023, 12, 5, 15, 30);

        // Act
        notification.setId(id);
        notification.setUserId(userId);
        notification.setType(type);
        notification.setMessage(message);
        notification.setRead(isRead);
        notification.setCreatedAt(createdAt);

        // Assert
        assertEquals(id, notification.getId());
        assertEquals(userId, notification.getUserId());
        assertEquals(type, notification.getType());
        assertEquals(message, notification.getMessage());
        assertTrue(notification.isRead());
        assertEquals(createdAt, notification.getCreatedAt());
    }

/*    @Test
    void testValidationConstraints() {
        // Arrange
        Notification notification = new Notification();
        notification.setId("3");
        notification.setUserId(null); // Invalid
        notification.setType(null);  // Invalid
        notification.setMessage(null); // Invalid
        notification.setRead(false);

        // Initialize the Validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // Act
        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);

        // Assert
        assertEquals(3, violations.size()); // userId, type, and message are @NotNull
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("userId")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("type")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("message")));
    }*/

    @Test
    void testDefaultValues() {
        // Act
        Notification notification = new Notification();

        // Assert
        assertFalse(notification.isRead());
        assertNotNull(notification.getCreatedAt());
    }
}