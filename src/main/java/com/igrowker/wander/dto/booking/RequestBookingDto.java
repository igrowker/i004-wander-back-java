package com.igrowker.wander.dto.booking;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestBookingDto {

    @NotBlank(message = "Experience ID is required")
    private String experienceId;

    @NotNull(message = "Booking date is required")
    private Date bookingDate;

    @NotNull(message = "Number of participants is required")
    @Min(value = 1, message = "At least one participant is required")
    private Integer participants;

    @NotBlank(message = "User ID is required")
    private String userId;
}

