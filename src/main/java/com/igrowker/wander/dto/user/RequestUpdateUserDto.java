package com.igrowker.wander.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUpdateUserDto {
    @NotBlank(message = "El nombre es obligatorio y no puede estar vacío.")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "El nombre solo debe contener letras.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
    private String name;

    @NotBlank(message = "El email es obligatorio y no puede estar vacío.")
    @Email(message = "Formato de email no válido.")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()]).{8,12}$",
            message = "La contraseña debe tener entre 8 y 12 caracteres, al menos una letra mayúscula, una letra minúscula, un número y un carácter especial (!@#$%^&*())."
    )
    private String password;

    @NotBlank(message = "El rol es obligatorio.")
    private String role;

    private String location;

    private List<String> preferences;

    @Pattern(regexp = "^(\\+?[0-9]{1,4})?[0-9]{7,14}$", message = "El teléfono debe ser válido.")
    private String phone;


    public void setName(String name) {
        if (name != null) {
            this.name = name.trim();
        } else {
            this.name = null;
        }
    }

    public void setRole(String role) {
        if (role != null) {
            this.role = role.toUpperCase();
        } else {
            this.role = null;
        }
    }
}

