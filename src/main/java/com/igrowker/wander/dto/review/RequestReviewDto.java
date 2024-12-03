package com.igrowker.wander.dto.review;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestReviewDto {

	@NotBlank(message = "El ID de la experiencia es obligatorio.")
    private String experienceId;

    @NotNull(message = "La calificación es obligatoria y debe estar entre 1 y 5.")
    @Min(value = 1, message = "La calificación mínima es 1.")
    @Max(value = 5, message = "La calificación máxima es 5.")
    private Double rating;

    @NotBlank(message = "El comentario es obligatorio.")
    @Size(min = 10, max = 500, message = "El comentario debe tener entre 10 y 500 caracteres.")
    private String comment;

}
