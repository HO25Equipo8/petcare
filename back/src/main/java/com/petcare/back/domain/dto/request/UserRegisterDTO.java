package com.petcare.back.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petcare.back.domain.entity.*;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import com.petcare.back.domain.enumerated.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;



public record UserRegisterDTO(
        String name,
        @Email
        String login,
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$",
                message = "Password must be at least 8 characters long, with at least one digit and one uppercase"
        )
        String pass1,
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$",
                message = "Password must be at least 8 characters long, with at least one digit and one uppercase"
        )

        String pass2,
        Role role,
        ProfessionalRoleEnum professionalRole,
        LocationDTO location
        ) {
}



