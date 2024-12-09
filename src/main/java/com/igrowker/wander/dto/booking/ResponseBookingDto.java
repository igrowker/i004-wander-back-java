package com.igrowker.wander.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.igrowker.wander.dto.user.UserDto;
import com.igrowker.wander.entity.enums.BookingStatus;
import com.igrowker.wander.entity.enums.PaymentStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseBookingDto {
    @NotNull(message = "ID is required")
    private String id;

    @NotNull(message = "Experience ID is required")
    private String experienceId;

    private String experienceTitle;

    private List<String> experienceImages;

    @NotNull(message = "User ID is required")
    private String userId;

    private UserDto tourist;

    private UserDto provider;

    @NotNull(message = "Booking status is required")
    private BookingStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Instant bookingDate;

    @NotNull(message = "Total price is required")
    private double totalPrice;

    @NotNull(message = "Number of participants is required")
    @Min(value = 1, message = "At least one participant is required")
    private Integer participants;

    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;

    @NotNull(message = "Creation date is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "UTC")
    private Date createdAt;

    public ResponseBookingDto(String number, Object o, Object o1, Object o2, Object o3) {
    }

    public void setDetails(String updatedDetails) {
    }
}

