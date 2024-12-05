package com.igrowker.wander.entity;

import com.igrowker.wander.entity.enums.PaymentStatus;
import com.igrowker.wander.entity.enums.BookingStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bookings")
public class BookingEntity {
    @Id
    private String id;

    @NotNull(message = "Experience ID is required")
    private String experienceId;

    @NotNull(message = "User ID is required")
    private String userId;

    @NotNull(message = "Status is required")
    private BookingStatus status;

    @NotNull(message = "Booking date is required")
    private Date bookingDate;

    @NotNull(message = "Total price is required")
    @Min(value = 0, message = "Total price must be non-negative")
    private double totalPrice;

    @NotNull(message = "Number of participants is required")
    @Min(value = 1, message = "At least one participant is required")
    private Integer participants;

    @NotNull(message = "The payment status cannot be null.")
    private PaymentStatus paymentStatus;

    private Date createdAt;

    public BookingEntity(String experienceId, String userId, BookingStatus status, Date bookingDate,
                         double totalPrice, Integer participants, PaymentStatus paymentStatus, Date date) {
        this.experienceId = experienceId;
        this.userId = userId;
        this.status = status;
        this.bookingDate = bookingDate;
        this.totalPrice = totalPrice;
        this.participants = participants;
        this.paymentStatus = paymentStatus;
        this.createdAt = new Date();
    }
}