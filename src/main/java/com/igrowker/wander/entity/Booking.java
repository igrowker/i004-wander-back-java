package com.igrowker.wander.entity;

import com.igrowker.wander.entity.enums.BookingStatus;
import com.igrowker.wander.entity.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;

    @NotBlank(message = "The experience ID cannot be empty or null.")
    private String experienceId;

    @NotBlank(message = "The user ID cannot be empty or null.")
    private String userId;

    @NotNull(message = "The booking status cannot be null.")
    private BookingStatus status = BookingStatus.PENDING;

    private LocalDateTime bookingDate;

    private double totalPrice;

    private int participants;

    @NotNull(message = "The payment status cannot be null.")
    private PaymentStatus paymentStatus;

    @CreatedDate
    private LocalDateTime createdAt;

}
