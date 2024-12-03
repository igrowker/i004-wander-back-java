package com.igrowker.wander.dto.experience;

import java.util.List;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestExperienceDto {

    @Size(min=3, max=50, message= "El título de la experiencia es obligatorio y debe tener entre 3 y 100 caracteres.")
    private String title;

    @NotBlank(message= "La descripción de la experiencia es obligatoria y debe tener entre 10 y 500 caracteres.")
    @Size(min = 10, max = 300, message = "La descripción debe tener entre 10 y 300 caracteres.")
    private String description;

    @NotNull(message = "La ubicación de la experiencia es obligatoria.")
    @Size(min = 1, message = "Debe proporcionar al menos un campo de ubicación.")
    private List<@NotBlank(message = "Cada elemento de la ubicación debe ser un texto válido.") String> location;

    @NotBlank
    private String hostId;

    @NotNull(message = "El precio es obligatorio.")
    @Min(value=1, message="El precio debe ser un valor positivo.")
    private Double price;

    @NotNull(message = "Las fechas de disponibilidad son obligatorias.")
    @Size(min = 1, message = "Debe haber al menos una fecha de disponibilidad.")
    private List<@NotBlank(message = "La fecha de disponibilidad no puede estar vacía.")
                    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$",
                    message = "El formato de la fecha debe ser 'yyyy-MM-dd HH:mm'.") String> availabilityDates;

    private List<@NotBlank(message = "Cada etiqueta debe ser un texto válido.") String> tags;

    @NotNull(message= "La capacidad debe ser al menos 1.")
    @Min(value = 1, message = "La capacidad debe ser al menos 1.")
    @Max(value = 50, message = "La capacidad no puede ser mayor de 50.")
    private int capacity;
}
