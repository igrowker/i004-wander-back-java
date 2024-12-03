package com.igrowker.wander.entity;

import com.igrowker.wander.entity.enums.PaymentStatus;
import com.igrowker.wander.entity.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bookings")
public class BookingEntity {

    @Id
    private String id;

    @NotNull(message = "Experience is required")
    private String experienceId;

    @NotNull(message = "User is required")
    private String userId;

    @NotNull(message = "Status is required")
    private BookingStatus status;

    @NotNull(message = "Booking date is required")
    private LocalDateTime bookingDate;

    @NotNull(message = "Total price is required")
    @Min(value = 0, message = "Total price must be non-negative")
    private double totalPrice;

    @NotNull(message = "Number of participants is required")
    @Min(value = 1, message = "At least one participant is required")
    private Integer participants;

    @NotNull(message = "The payment status cannot be null.")
    private PaymentStatus paymentStatus;

    private LocalDateTime createdAt;

    // Constructor to initialize createdAt
    public BookingEntity(String id, String experienceId, String userId, BookingStatus status, LocalDateTime bookingDate,
                         double totalPrice, Integer participants, PaymentStatus paymentStatus) {
        this.id = id;
        this.experienceId = experienceId;
        this.userId = userId;
        this.status = status;
        this.bookingDate = bookingDate;
        this.totalPrice = totalPrice;
        this.participants = participants;
        this.paymentStatus = paymentStatus;
        this.createdAt = LocalDateTime.now();
    }

    // Setters for specific fields
    public void setExperienceId(String experienceId) {
        this.experienceId = experienceId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
