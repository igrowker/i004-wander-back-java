package com.igrowker.wander.dto.booking;

import com.igrowker.wander.entity.enums.BookingStatus;
import com.igrowker.wander.entity.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBookingDto {
    private String id;
    private String experienceId;
    private String userId;
    private BookingStatus status;
    private Date bookingDate;
    private double totalPrice;
    private int participants;
    private PaymentStatus paymentStatus;
    private Date createdAt;
}

