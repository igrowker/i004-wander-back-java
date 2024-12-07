package com.igrowker.wander.dto.experience;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityDateWithSlotsDto {
    private String date;
    private int availableSlots;
}
