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
import com.petcare.back.repository.UserRepository;
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
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    @Transactional
    public OfferingResponseDTO createService(OfferingCreateDTO dto) throws MyException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo los profesionales pueden registrar servicios");
        }

        if(!user.isVerified()){
            if (user.getProfileComplete()) {
                throw new MyException("Debes estar verificado para poder crear servicios, tu perfil está en evaluación");
            }else{
                throw new MyException("Debes completa tu perfil para poder estar verificado");
            }
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

    public List<OfferingResponseDTO> getBySitter(User sitter) {
        return repository.findBySitterId(sitter.getId()).stream()
                .map(responseMapper::toDto)
                .toList();
    }

    public OfferingResponseDTO getById(Long id) throws MyException {
        Offering offering = repository.findById(id)
                .orElseThrow(() -> new MyException("Servicio no encontrado"));
        return responseMapper.toDto(offering);
    }

    @Transactional
    public OfferingResponseDTO update(Long id, User sitter, OfferingCreateDTO dto) throws MyException {

        Offering offering = repository.findById(id)
                .orElseThrow(() -> new MyException("Servicio no encontrado"));

        if (!offering.getSitter().getId().equals(sitter.getId())) {
            throw new MyException("No tenés permiso para modificar este servicio");
        }

        if (dto.name() != null) {
            offering.setName(dto.name());
        }
        if (dto.description() != null) {
            offering.setDescription(dto.description());
        }
        if (dto.allowedRole() != null) {
            offering.setAllowedRole(dto.allowedRole());
        }
        if (dto.basePrice() != null) {
            offering.setBasePrice(dto.basePrice());
        }
        if (dto.applicablePetTypes() != null && !dto.applicablePetTypes().isEmpty()) {
            offering.setApplicablePetTypes(dto.applicablePetTypes());
        }

        repository.save(offering);

        return responseMapper.toDto(offering);
    }

    @Transactional
    public void softDelete(Long id, User sitter) throws MyException {
        Offering offering = repository.findById(id)
                .orElseThrow(() -> new MyException("Servicio no encontrado"));

        if (!offering.getSitter().getId().equals(sitter.getId())) {
            throw new MyException("No tenés permiso para eliminar este servicio");
        }

        offering.setActive(false);
        repository.save(offering);
    }

    public void activeOffering(Long id, User sitter) throws MyException {
        Offering offering = repository.findById(id)
                .orElseThrow(() -> new MyException("Servicio no encontrado"));

        if (!offering.getSitter().getId().equals(sitter.getId())) {
            throw new MyException("No tenés permiso para activar este servicio");
        }

        offering.setActive(true);
        repository.save(offering);
    }

    public List<OfferingResponseDTO> getPublicOfferingsBySitter(Long sitterId) throws MyException {
        User sitter = userRepository.findById(sitterId)
                .orElseThrow(() -> new MyException("Profesional no encontrado"));

        if (!sitter.isVerified()) {
            throw new MyException("Este profesional aún no está verificado");
        }

        return repository.findBySitterIdAndActiveTrue(sitterId).stream()
                .map(responseMapper::toDto)
                .toList();
    }

}
