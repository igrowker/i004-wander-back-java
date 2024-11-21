package com.igrowker.wander.entity;

import com.igrowker.wander.entity.enums.BookingStatus;
import com.igrowker.wander.entity.enums.PaymentStatus;
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

    @NotNull
    private String experienceId;

    @NotNull
    private String userId;

    @NotNull
    private BookingStatus status = BookingStatus.PENDING;

    @CreatedDate
    private LocalDateTime bookingDate;

    @NotNull
    private double totalPrice;

    @Positive
    private int participants;

    @NotNull
    private PaymentStatus paymentStatus;
}
