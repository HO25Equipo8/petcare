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

    public ComboOfferingResponseDTO create(ComboOfferingCreateDTO dto) throws MyException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.ADMIN) {
            throw new MyException("Solo los admin pueden registrar combos");
        }

        List<Offering> offerings = offeringRepository.findAllById(dto.offeringIds());

        for (ValidationCombo v : validations) {
            v.validate(dto, offerings);
        }

        ComboOffering combo = comboOfferingRepository.save(mapper.toEntity(dto, offerings));

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
