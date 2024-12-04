package com.igrowker.wander.entityTest;

import com.igrowker.wander.entity.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;
    private Validator validator;

    @BeforeEach
    void setUp() {
        // Initialize the user object before each test
        user = new User();
        user.setId("1");
        user.setEmail("test@example.com");
        user.setName("TestUser");
        user.setPassword("securePass123");
        user.setRole("TOURIST");
        user.setAvatar("https://medvirturials.com/img/default-image.png");
        user.setEnabled(true);
        user.setPreferences(List.of("Hiking", "Photography"));
        user.setLocation("New York");
        user.setCreatedAt(LocalDateTime.now());
        user.setBookings(List.of(123L, 456L));
        user.setVerificationCode("ABC123");
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
        user.setPasswordResetCode("XYZ789");
        user.setPasswordResetCodeExpiresAt(LocalDateTime.now().plusHours(1));

        // Initialize validator for testing constraints
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testUserDetailsImplementation() {
        // Check authorities
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_TOURIST", authorities.iterator().next().getAuthority());

        // Check username is email
        assertEquals("test@example.com", user.getUsername());

        // Account status
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testValidUser() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void testInvalidEmail() {
        user.setEmail("invalid-email");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Expected validation errors for invalid email");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("must be a well-formed email address")));
    }

    @Test
    void testNullEmail() {
        // Arrange
        user.setEmail(null);

        // Act
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Assert
        assertFalse(violations.isEmpty(), "Expected validation errors for null email");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The email is required")));
    }

    @Test
    void testShortPassword() {
        user.setPassword("123");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Expected validation errors for short password");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The password is too short")));
    }

    @Test
    void testNullPassword() {
        user.setPassword(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Expected validation errors for null password");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The password is required")));
    }

    @Test
    void testDefaultValues() {
        User newUser = new User();
        assertFalse(newUser.isEnabled(), "Expected default 'enabled' to be false");
        assertEquals("TOURIST", newUser.getRole(), "Expected default role to be 'TOURIST'");
        assertEquals("https://medvirturials.com/img/default-image.png", newUser.getAvatar(), "Expected default avatar URL");
        assertNotNull(newUser.getCreatedAt(), "Expected default creation date to be set");
    }
}
