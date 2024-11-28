package com.igrowker.wander.dto.experience;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceReservationCountDto {

    @Field("_id")
    private String experienceId;
    private long count;
}
