package com.igrowker.wander.dto.experience;

import java.util.List;
import jakarta.validation.constraints.Max;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestExperienceDto {

    @Size(min=3, max=100, message= "El título de la experiencia es obligatorio y debe tener entre 3 y 100 caracteres.")
    private String title;

    @NotBlank(message= "La descripción de la experiencia es obligatoria y debe tener entre 10 y 500 caracteres.")
    private String description;

    @NotBlank(message= "La ubicación de la experiencia es obligatoria.")
    private String location;

    @NotBlank
    private String hostId; 

    @Min(value=1, message="El precio debe ser un valor positivo.")
    private Double price;

    private List<String> availabilityDates;

    private List<String> tags;

    private Double rating;

    @NotNull(message= "La capacidad debe ser al menos 1.")
    @Min(value = 1, message = "La capacidad debe ser al menos 1.")
    @Max(value = 50, message = "La capacidad no puede ser mayor de 50.")
    private int capacity;
}
