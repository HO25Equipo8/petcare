package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ValidateOfferingRoleCompatibility implements ValidationOffering {

    private static final Map<OfferingEnum, ProfessionalRoleEnum> requiredRoles = Map.of(
            OfferingEnum.ASEO, ProfessionalRoleEnum.PELUQUERO,
            OfferingEnum.PASEO, ProfessionalRoleEnum.PASEADOR,
            OfferingEnum.GUARDERIA, ProfessionalRoleEnum.CUIDADOR,
            OfferingEnum.VETERINARIA, ProfessionalRoleEnum.VETERINARIO
    );
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    @Override
    public void validar(OfferingCreateDTO dto) throws MyException {
        ProfessionalRoleEnum requiredRole = requiredRoles.get(dto.name());
        log.debug("Rol recibido: {}", dto.allowedRole()); // o dto.allowedRol() según el nombre

        if (requiredRole != null) {
            if (dto.allowedRole() == null || !dto.allowedRole().equals(requiredRole)) {
                throw new MyException("El servicio " + dto.name().getLabel() +
                        " solo puede ser realizado por profesionales con rol " + requiredRole.name() + ".");
            }
        }
    }
}
