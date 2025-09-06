package com.petcare.back.service;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.dto.response.ComboOfferingResponseDTO;
import com.petcare.back.domain.entity.ComboOffering;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.request.ComboCreateMapper;
import com.petcare.back.domain.mapper.request.OfferingCreateMapper;
import com.petcare.back.domain.mapper.response.ComboResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ComboOfferingRepository;
import com.petcare.back.repository.OfferingRepository;
import com.petcare.back.validation.WarningComboEvaluator;
import com.petcare.back.validation.ValidationCombo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ComboOfferingService {

    private final ComboOfferingRepository comboOfferingRepository;
    private final OfferingRepository offeringRepository;
    private final ComboCreateMapper mapper;
    private final ComboResponseMapper comboResponseMapper;
    private final OfferingCreateMapper offeringCreateMapper;
    private final WarningComboEvaluator warningComboEvaluator;
    private final List<ValidationCombo> validations;

    @Transactional
    public ComboOfferingResponseDTO create(ComboOfferingCreateDTO dto) throws MyException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo los profesionales pueden registrar combos.");
        }

        List<Offering> offerings = offeringRepository.findAllById(dto.offeringIds());

        // Validación crítica: todos los servicios deben pertenecer al profesional autenticado
        boolean todosSonDelUsuario = offerings.stream()
                .allMatch(o -> o.getSitter() != null && o.getSitter().getId().equals(user.getId()));

        if (!todosSonDelUsuario) {
            throw new MyException("Solo podés crear combos con servicios que vos ofrecés. Verificá que todos los IDs correspondan a tus servicios.");
        }

        for (ValidationCombo v : validations) {
            v.validate(dto, offerings);
        }

        ComboOffering combo = mapper.toEntity(dto, offerings);
        combo.setSitter(user);
        comboOfferingRepository.save(combo);

        List<String> warnings = warningComboEvaluator.evaluar(dto, offerings);
        if (warnings.isEmpty()) {
            warnings.add("✅ No se detectaron contradicciones visibles en este combo.");
        }

        return new ComboOfferingResponseDTO(
                combo.getId(),
                combo.getName(),
                combo.getDescription(),
                dto.discount(),
                combo.getFinalPrice(),
                offerings.stream()
                        .map(offeringCreateMapper::toDto)
                        .toList(),
                warnings
        );
    }

    public List<ComboOfferingResponseDTO> findAll() {
        return comboOfferingRepository.findAll().stream()
                .map(comboResponseMapper::toResponse)
                .toList();
    }
}
