package com.igrowker.wander.dto.booking;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestBookingDto {

    @NotNull(message = "Experience ID is required")
    private String experienceId;

    @NotNull(message = "Booking date is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "UTC") // Usamos JsonFormat para LocalDateTime
    private LocalDateTime bookingDate; // Usando LocalDateTime en lugar de Date

    @NotNull(message = "Number of participants is required")
    @Min(value = 1, message = "At least one participant is required")
    private Integer participants;

    @NotNull(message = "User ID is required")
    private String userId; // Implementé el getter correctamente, ahora userId está en el DTO
}

