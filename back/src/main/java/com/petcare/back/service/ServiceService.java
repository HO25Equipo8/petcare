package com.petcare.back.service;

import com.petcare.back.domain.dto.request.ServiceCreateDTO;
import com.petcare.back.domain.dto.response.ServiceResponseDTO;
import com.petcare.back.domain.entity.Service;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.request.ServiceCreateMapper;
import com.petcare.back.domain.mapper.response.ServiceResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository repository;
    private final ServiceCreateMapper mapper;
    private final ServiceResponseMapper responseMapper;

    public ServiceResponseDTO createService(ServiceCreateDTO dto) throws MyException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.ADMIN) {
            throw new MyException("Solo los admin pueden registrar servicios");
        }
        Service entity = mapper.toEntity(dto);
        repository.save(entity);
        return responseMapper.toDto(entity);
    }
}
