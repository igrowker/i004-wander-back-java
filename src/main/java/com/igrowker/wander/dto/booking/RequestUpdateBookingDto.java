package com.igrowker.wander.dto.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.igrowker.wander.entity.enums.BookingStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUpdateBookingDto {

    @NotNull(message = "User id is required")
    private String userId;

    @NotNull(message = "Status is required")
    private BookingStatus status;

    public void setDetails(String updatedDetails) {

    }
}
