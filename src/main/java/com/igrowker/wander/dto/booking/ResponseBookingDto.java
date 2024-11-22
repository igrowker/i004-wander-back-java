package com.igrowker.wander.dto.booking;

import com.igrowker.wander.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBookingDto {

    private String id;

    private String experienceId;

    private String userId;

    private BookingStatus status;

    private Date bookingDate;

    private double totalPrice;

    private Integer participants;

    private String paymentStatus;

    private Date createdAt;
}
