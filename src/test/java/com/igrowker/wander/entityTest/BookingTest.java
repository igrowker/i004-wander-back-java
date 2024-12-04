package com.igrowker.wander.entityTest;

import com.igrowker.wander.entity.BookingEntity;
import com.igrowker.wander.entity.enums.BookingStatus;
import com.igrowker.wander.entity.enums.PaymentStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    private BookingEntity booking;
    private Validator validator;

    @BeforeEach
    void setUp() {
        booking = new BookingEntity();
        booking.setId("1");
        booking.setExperienceId("exp123");
        booking.setUserId("user123");
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setBookingDate(new Date());
        booking.setTotalPrice(100.0);
        booking.setParticipants(2);
        booking.setPaymentStatus(PaymentStatus.COMPLETED);
        booking.setCreatedAt(new Date());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        assertNotNull(validator, "Validator should be initialized");
    }

    @Test
    void testBookingEntityInitialization() {
        assertNotNull(booking, "BookingEntity should be initialized");
    }

    @Test
    void testValidBooking() {
        Set<ConstraintViolation<BookingEntity>> violations = validator.validate(booking);
        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid BookingEntity");
    }

    @Test
    void testDefaultValues() {
        BookingEntity newBooking = new BookingEntity();
        assertNotNull(newBooking.getCreatedAt(), "Expected default 'createdAt' to be set");
    }

    @Test
    void testSetters() {
        booking.setExperienceId(null);
        assertNull(booking.getExperienceId(), "ExperienceId should be null after setting it");
    }
}

/*    @Test
    void testInvalidExperienceId() {
        booking.setExperienceId(null);
        Set<ConstraintViolation<BookingEntity>> violations = validator.validate(booking);
        assertFalse(violations.isEmpty(), "Expected validation errors for null experienceId");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Experience is required")));
    }

    @Test
    void testInvalidUserId() {
        booking.setUserId(null);
        Set<ConstraintViolation<BookingEntity>> violations = validator.validate(booking);
        assertFalse(violations.isEmpty(), "Expected validation errors for null userId");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("User is required")));
    }

    @Test
    void testInvalidTotalPrice() {
        booking.setTotalPrice(-1.0);
        Set<ConstraintViolation<BookingEntity>> violations = validator.validate(booking);
        assertFalse(violations.isEmpty(), "Expected validation errors for negative totalPrice");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Total price must be non-negative")));
    }

    @Test
    void testInvalidParticipants() {
        booking.setParticipants(0);
        Set<ConstraintViolation<BookingEntity>> violations = validator.validate(booking);
        assertFalse(violations.isEmpty(), "Expected validation errors for invalid number of participants");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("At least one participant is required")));
    }

    @Test
    void testInvalidPaymentStatus() {
        booking.setPaymentStatus(null);
        Set<ConstraintViolation<BookingEntity>> violations = validator.validate(booking);
        assertFalse(violations.isEmpty(), "Expected validation errors for null paymentStatus");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The payment status cannot be null.")));
    }
}*/