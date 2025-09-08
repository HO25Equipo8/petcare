package com.petcare.back.service;

import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.domain.dto.response.OfferingResponseDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.request.OfferingCreateMapper;
import com.petcare.back.domain.mapper.response.OfferingResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.OfferingRepository;
import com.petcare.back.validation.ValidationOffering;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OfferingService {

    private final OfferingRepository repository;
    private final OfferingCreateMapper mapper;
    private final OfferingResponseMapper responseMapper;
    private final List<ValidationOffering> validations;
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    @Transactional
    public OfferingResponseDTO createService(OfferingCreateDTO dto) throws MyException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo los profesionales pueden registrar servicios");
        }

        List<ProfessionalRoleEnum> roles = user.getProfessionalRoles();
        if (roles == null || !roles.contains(dto.allowedRole())) {
            throw new MyException("No tenés el rol '" + dto.allowedRole().name() + "' habilitado en tu perfil para registrar este servicio.");
        }

        for (ValidationOffering v : validations) {
            v.validate(dto);
        }

        try {
            Offering offering = mapper.toEntity(dto);
            offering.setActive(true);
            offering.setSitter(user);
            repository.save(offering);
            return responseMapper.toDto(offering);
        } catch (Exception e) {
            log.error("Error al registrar el servicio", e); // Esto mostrará el stacktrace real
            throw new MyException("No se pudo registrar el servicio. Revisá los datos o contactá al equipo.");
        }
    }
}
