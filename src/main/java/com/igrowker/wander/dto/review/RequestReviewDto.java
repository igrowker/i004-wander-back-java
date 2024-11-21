package com.igrowker.wander.dto.review;

import java.util.Date;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestReviewDto {

	@NotBlank(message = "El ID de la experiencia es obligatorio.")
    private String experienceId; 

    @NotBlank(message = "El ID del usuario es obligatorio.")
    private String userId;

    @NotBlank(message = "La calificación es obligatoria y debe estar entre 1 y 5.")
    @Min(value = 1, message = "La calificación mínima es 1.")
    @Max(value = 5, message = "La calificación máxima es 5.")
    private Double rating; 

    private String comment;

    @NotBlank(message = "La fecha de creación es obligatoria.")
    private Date createdAt;
}
