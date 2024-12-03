package com.igrowker.wander.dto.booking;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestBookingDto {

    @NotNull(message = "Experience ID is required")
    private String experienceId;

    @NotNull(message = "Booking date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date bookingDate;

    @NotNull(message = "Number of participants is required")
    @Min(value = 1, message = "At least one participant is required")
    private Integer participants;
}
