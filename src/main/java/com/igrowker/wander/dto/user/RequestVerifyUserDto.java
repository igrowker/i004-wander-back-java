package com.igrowker.wander.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestVerifyUserDto {
    @NotBlank(message = "El email es obligatorio y no puede estar vacío.")
    @Email(message = "Formato de email no válido.")
    private String email;
    @NotBlank(message = "El código de verificación es obligatorio")
    @Size(min = 6, max = 6, message = "El código de verificación debe contener 6 números.")
    private String verificationCode;
}
