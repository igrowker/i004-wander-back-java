package com.igrowker.wander.entityTest;

import com.igrowker.wander.entity.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class NotificationTest {

    @InjectMocks
    private Notification notification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notification = new Notification();
    }

    @Test
    void testNotificationInitialization() { assertNotNull(notification);
    }

    @Test
    void testSettersAndGetters() {
        String id = "1";
        String userId = "user123";
        String type = "ALERT";
        String message = "This is a test notification.";
        Boolean isRead = true;
        LocalDateTime createdAt = LocalDateTime.now();
        notification.setId(id);
        notification.setUserId(userId);
        notification.setType(type);
        notification.setMessage(message);
        notification.setIsRead(isRead);
        notification.setCreatedAt(createdAt);
        assertEquals(id, notification.getId());
        assertEquals(userId, notification.getUserId());
        assertEquals(type, notification.getType());
        assertEquals(message, notification.getMessage());
        assertEquals(isRead, notification.getIsRead());
        assertEquals(createdAt, notification.getCreatedAt());
    }
    @Test
    void testDefaultValues() {
        Notification newNotification = new Notification();
        assertFalse(newNotification.getIsRead());
        assertNotNull(newNotification.getCreatedAt());
    }
    @Test
    void testNullValues() {
        Exception exceptionUserId = assertThrows(NullPointerException.class, () -> notification.setUserId(null));
        assertEquals("userId is marked non-null but is null", exceptionUserId.getMessage());
        Exception exceptionType = assertThrows(NullPointerException.class, () -> notification.setType(null));
        assertEquals("type is marked non-null but is null", exceptionType.getMessage());
        Exception exceptionMessage = assertThrows(NullPointerException.class, () -> notification.setMessage(null));
        assertEquals("message is marked non-null but is null", exceptionMessage.getMessage());
    }
}