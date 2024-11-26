/*package com.igrowker.wander.entityTest;

import com.igrowker.wander.entity.BookingEntity;
import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.entity.enums.BookingStatus;
import com.igrowker.wander.entity.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidBookingEntity() {
        // Arrange
        ExperienceEntity mockExperience = new ExperienceEntity();
        User mockUser = new User();

        BookingEntity booking = new BookingEntity(
                "1",
                mockExperience, // Mock ExperienceEntity
                mockUser, // Mock User
                BookingStatus.CONFIRMED,
                new Date(),
                100.0,
                2,
                PaymentStatus.PAID,
                new Date()
        );

        // Act
        Set<ConstraintViolation<BookingEntity>> violations = validator.validate(booking);

        // Assert
        assertTrue(violations.isEmpty(), "Valid BookingEntity should not have any validation errors.");
    }

    @Test
    void testInvalidBookingEntity_NullFields() {
        // Arrange
        BookingEntity booking = new BookingEntity();

        // Act
        Set<ConstraintViolation<BookingEntity>> violations = validator.validate(booking);

        // Assert
        assertFalse(violations.isEmpty(), "BookingEntity with null fields should have validation errors.");
        assertEquals(7, violations.size(), "Expected 7 validation errors for null fields.");
    }

    @Test
    void testInvalidBookingEntity_NegativeTotalPrice() {
        // Arrange
        ExperienceEntity mockExperience = new ExperienceEntity();
        User mockUser = new User();

        BookingEntity booking = new BookingEntity(
                "1",
                mockExperience,
                mockUser,
                BookingStatus.CONFIRMED,
                new Date(),
                -10.0, // Invalid total price
                2,
                PaymentStatus.PAID,
                new Date()
        );

        // Act
        Set<ConstraintViolation<BookingEntity>> violations = validator.validate(booking);

        // Assert
        assertFalse(violations.isEmpty(), "BookingEntity with negative total price should have validation errors.");
        assertTrue(
                violations.stream().anyMatch(v -> v.getMessage().equals("Total price must be non-negative")),
                "Validation should detect negative total price."
        );
    }

    @Test
    void testInvalidBookingEntity_ZeroParticipants() {
        // Arrange
        ExperienceEntity mockExperience = new ExperienceEntity();
        User mockUser = new User();

        BookingEntity booking = new BookingEntity(
                "1",
                mockExperience,
                mockUser,
                BookingStatus.CONFIRMED,
                new Date(),
                100.0,
                0, // Invalid number of participants
                PaymentStatus.PAID,
                new Date()
        );

        // Act
        Set<ConstraintViolation<BookingEntity>> violations = validator.validate(booking);

        // Assert
        assertFalse(violations.isEmpty(), "BookingEntity with zero participants should have validation errors.");
        assertTrue(
                violations.stream().anyMatch(v -> v.getMessage().equals("At least one participant is required")),
                "Validation should detect zero participants."
        );
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        ExperienceEntity mockExperience = new ExperienceEntity();
        User mockUser = new User();

        BookingEntity booking1 = new BookingEntity("1", mockExperience, mockUser, BookingStatus.CONFIRMED, new Date(), 100.0, 2, PaymentStatus.PAID, new Date());
        BookingEntity booking2 = new BookingEntity("1", mockExperience, mockUser, BookingStatus.CONFIRMED, new Date(), 100.0, 2, PaymentStatus.PAID, new Date());

        // Act & Assert
        assertEquals(booking1, booking2, "Two bookings with the same ID should be equal.");
        assertEquals(booking1.hashCode(), booking2.hashCode(), "Hash codes for equal bookings should match.");
    }

    @Test
    void testToString() {
        // Arrange
        ExperienceEntity mockExperience = new ExperienceEntity();
        User mockUser = new User();

        BookingEntity booking = new BookingEntity(
                "1",
                mockExperience,
                mockUser,
                BookingStatus.CONFIRMED,
                new Date(),
                100.0,
                2,
                PaymentStatus.PAID,
                new Date()
        );

        // Act
        String result = booking.toString();

        // Assert
        assertNotNull(result, "toString() should not return null.");
        assertTrue(result.contains("BookingEntity"), "toString() should contain the class name.");
        assertTrue(result.contains("id=1"), "toString() should contain the ID.");
    }
}*/