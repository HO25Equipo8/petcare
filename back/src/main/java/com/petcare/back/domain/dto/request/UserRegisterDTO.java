package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegisterDTO(

        @NotBlank(message = "Email no puede estar vacío")
        @Email
        String login,
        @NotBlank(message = "Contraseña no puede estar vacía")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$",
                message = "Contraseña deber tener 8 caracteres, con al menos un digito y una mayúscula"
        )
        String pass1,
        @NotBlank(message = "Contraseña no puede estar vacía")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$",
                message = "Contraseña deber tener 8 caracteres, con al menos un digito y una mayúscula"
        )
        String pass2,
        Role role
) {

}



