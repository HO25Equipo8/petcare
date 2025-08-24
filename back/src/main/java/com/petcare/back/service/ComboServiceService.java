package com.petcare.back.service;

import com.petcare.back.domain.dto.request.ComboServiceCreateDTO;
import com.petcare.back.domain.dto.response.ComboServiceResponseDTO;
import com.petcare.back.domain.entity.ComboService;
import com.petcare.back.domain.entity.Service;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.request.ComboCreateMapper;
import com.petcare.back.domain.mapper.response.ComboResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ComboServiceRepository;
import com.petcare.back.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ComboServiceService {

    private final ComboServiceRepository comboServiceRepository;
    private final ServiceRepository serviceRepository;
    private final ComboCreateMapper mapper;
    private final ComboResponseMapper comboResponseMapper;

    public ComboServiceResponseDTO create(ComboServiceCreateDTO dto) throws MyException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.ADMIN) {
            throw new MyException("Solo los admin pueden registrar combos");
        }

        List<Service> services = serviceRepository.findAllById(dto.serviceIds());
        ComboService combo = mapper.toEntity(dto, services);
        return comboResponseMapper.toResponse(comboServiceRepository.save(combo));
    }

    public List<ComboServiceResponseDTO> findAll() {
        return comboServiceRepository.findAll().stream()
                .map(comboResponseMapper::toResponse)
                .toList();
    }
}
