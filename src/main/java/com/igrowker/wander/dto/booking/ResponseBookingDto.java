package com.igrowker.wander.dto.booking;

import com.igrowker.wander.entity.enums.BookingStatus;
import com.igrowker.wander.entity.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBookingDto {
    private String id;
    private String experienceId;
    private String userId;
    private BookingStatus status;
    private LocalDateTime bookingDate;
    private double totalPrice;
    private int participants;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
}
