package com.petcare.back.service;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.dto.request.ComboOfferingUpdateDTO;
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
import com.petcare.back.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public ComboOfferingResponseDTO create(ComboOfferingCreateDTO dto) throws MyException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo los profesionales pueden registrar combos.");
        }
        if(!user.isChecked()){
            if (user.getProfileComplete()) {
                throw new MyException("Debes estar verificado para poder crear combos, tu perfil está en evaluación");
            }else{
                throw new MyException("Debes completa tu perfil para poder estar verificado");
            }
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

    @Transactional
    public ComboOfferingResponseDTO update(Long id, User sitter, ComboOfferingUpdateDTO dto) throws MyException {
        ComboOffering combo = comboOfferingRepository.findById(id)
                .orElseThrow(() -> new MyException("Combo no encontrado"));

        if (!combo.getSitter().getId().equals(sitter.getId())) {
            throw new MyException("No tenés permiso para modificar este combo");
        }

        if (dto.name() != null) combo.setName(dto.name());
        if (dto.description() != null) combo.setDescription(dto.description());
        if (dto.discount() != null) combo.setDiscount(dto.discount());

        // Actualizar servicios si se envían nuevos IDs
        if (dto.offeringIds() != null && !dto.offeringIds().isEmpty()) {
            List<Offering> offerings = offeringRepository.findAllById(dto.offeringIds());

            boolean todosSonDelUsuario = offerings.stream()
                    .allMatch(o -> o.getSitter() != null && o.getSitter().getId().equals(sitter.getId()));

            if (!todosSonDelUsuario) {
                throw new MyException("Solo podés actualizar combos con servicios que vos ofrecés.");
            }

            combo.setOfferings(offerings);
            combo.getFinalPrice();
        }
        return comboResponseMapper.toResponse(combo);
    }

    public ComboOfferingResponseDTO getById(Long id) throws MyException {
        ComboOffering combo = comboOfferingRepository.findById(id)
                .orElseThrow(() -> new MyException("Combo no encontrado"));
        return comboResponseMapper.toResponse(combo);
    }

    public List<ComboOfferingResponseDTO> getBySitter(User sitter) {
        return comboOfferingRepository.findBySitterId(sitter.getId()).stream()
                .map(comboResponseMapper::toResponse)
                .toList();
    }

    public List<ComboOfferingResponseDTO> findAll() {
        return comboOfferingRepository.findAll().stream()
                .map(comboResponseMapper::toResponse)
                .toList();
    }

    public List<ComboOfferingResponseDTO> getPublicCombosBySitter(Long sitterId) throws MyException {
        User sitter = userRepository.findById(sitterId)
                .orElseThrow(() -> new MyException("Profesional no encontrado"));

        if (!sitter.isChecked()) {
            throw new MyException("Este profesional aún no está verificado");
        }

        return comboOfferingRepository.findBySitterId(sitterId).stream()
                .map(comboResponseMapper::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long id, User sitter) throws MyException {
        ComboOffering combo = comboOfferingRepository.findById(id)
                .orElseThrow(() -> new MyException("Combo no encontrado"));

        if (!combo.getSitter().getId().equals(sitter.getId())) {
            throw new MyException("No tenés permiso para eliminar este combo");
        }
        comboOfferingRepository.delete(combo);
    }
}
