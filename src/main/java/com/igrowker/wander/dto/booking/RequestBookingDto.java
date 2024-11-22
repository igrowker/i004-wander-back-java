package com.igrowker.wander.dto.booking;

import com.igrowker.wander.entity.enums.BookingStatus;
import com.igrowker.wander.entity.enums.PaymentStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBookingDto {

    private BookingStatus status;

    private PaymentStatus paymentStatus;

    @FutureOrPresent(message = "Booking date must be in the future or today.")
    private LocalDateTime bookingDate;

    @Positive(message = "The number of participants must be positive.")
    private Integer participants;

    @Positive(message = "The total price must be positive.")
    private Double totalPrice;
}
