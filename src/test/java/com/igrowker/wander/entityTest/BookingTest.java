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

import java.time.LocalDateTime;
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
    void validBookingEntityShouldPassValidation() {
        BookingEntity booking = new BookingEntity(
                "1",
                "exp123",
                "user123",
                BookingStatus.PENDING,
                new Date(), // Usando Date en lugar de LocalDateTime
                100.0,
                2,
                PaymentStatus.PAID,
                new Date()
        );

        Set<ConstraintViolation<BookingEntity>> violations = validator.validate(booking);
        assertTrue(violations.isEmpty(), "There should be no validation errors for a valid entity");
    }

    @Test
    void invalidBookingEntityShouldFailValidation() {
        BookingEntity booking = new BookingEntity(
                null, // Invalid: id is null (optional, depending on use case)
                null, // Invalid: experienceId is null
                null, // Invalid: userId is null
                null, // Invalid: status is null
                null, // Invalid: bookingDate is null
                -50.0, // Invalid: totalPrice is negative
                0, // Invalid: participants less than 1
                null, // Invalid: paymentStatus is null
                new Date() // Añadiendo Date como createdAt
        );

        Set<ConstraintViolation<BookingEntity>> violations = validator.validate(booking);
        assertFalse(violations.isEmpty(), "There should be validation errors for an invalid entity");

        assertEquals(7, violations.size(), "Expected 7 validation errors");
    }

    @Test
    void shouldFailWhenTotalPriceIsNegative() {
        BookingEntity booking = new BookingEntity("1", "exp123", "user123", BookingStatus.PENDING, new Date(), 100.0, 2, PaymentStatus.PENDING, new Date());
        booking.setExperienceId("exp123");
        booking.setUserId("user123");
        booking.setStatus(BookingStatus.PENDING);
        booking.setBookingDate(new Date()); // Usando Date
        booking.setTotalPrice(-10.0); // Invalid: negative price
        booking.setParticipants(2);
        booking.setPaymentStatus(PaymentStatus.PAID);

        Set<ConstraintViolation<BookingEntity>> violations = validator.validate(booking);
        assertFalse(violations.isEmpty(), "Total price validation should fail for negative value");

        ConstraintViolation<BookingEntity> violation = violations.iterator().next();
        assertEquals("Total price must be non-negative", violation.getMessage());
    }

    @Test
    void shouldFailWhenParticipantsAreLessThanOne() {
        BookingEntity booking = new BookingEntity("1", "exp123", "user123", BookingStatus.PENDING, new Date(), 100.0, 2, PaymentStatus.PENDING, new Date());
        booking.setExperienceId("exp123");
        booking.setUserId("user123");
        booking.setStatus(BookingStatus.PENDING);
        booking.setBookingDate(new Date()); // Usando Date
        booking.setTotalPrice(50.0);
        booking.setParticipants(0); // Invalid: participants less than 1
        booking.setPaymentStatus(PaymentStatus.PAID);

        Set<ConstraintViolation<BookingEntity>> violations = validator.validate(booking);
        assertFalse(violations.isEmpty(), "Participants validation should fail for value less than 1");

        ConstraintViolation<BookingEntity> violation = violations.iterator().next();
        assertEquals("At least one participant is required", violation.getMessage());
    }
}
