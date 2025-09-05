package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.UserUpdateDTO;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidateUserUpdateProfileProfessionalRoles implements ValidationUserProfile{

    @Override
    public void validate(UserUpdateDTO dto, User user) throws MyException {
        if (user.getRole() != Role.SITTER) return;

        if (dto.professionalRoles() == null || dto.professionalRoles().isEmpty()) {
            throw new MyException("Un sitter debe tener al menos un rol profesional.");
        }

        List<ProfessionalRoleEnum> valid = dto.professionalRoles().stream()
                .filter(role -> role == ProfessionalRoleEnum.PASEADOR ||
                        role == ProfessionalRoleEnum.VETERINARIO ||
                        role == ProfessionalRoleEnum.PELUQUERO ||
                        role == ProfessionalRoleEnum.CUIDADOR)
                .toList();

        if (valid.isEmpty()) {
            throw new MyException("Los roles enviados no son válidos para un sitter.");
        }
    }
}
