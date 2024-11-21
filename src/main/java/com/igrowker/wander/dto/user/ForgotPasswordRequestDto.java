
package com.igrowker.wander.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequestDto {

    @NotBlank(message = "El email es obligatorio y no puede estar vacío.")
    @Email(message = "El formato del email no es válido.")
    private String email;

}
