package com.petcare.back.service;

import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.domain.dto.response.OfferingResponseDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.request.OfferingCreateMapper;
import com.petcare.back.domain.mapper.response.OfferingResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.OfferingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OfferingService {

    private final OfferingRepository repository;
    private final OfferingCreateMapper mapper;
    private final OfferingResponseMapper responseMapper;

    public OfferingResponseDTO createService(OfferingCreateDTO dto) throws MyException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.ADMIN) {
            throw new MyException("Solo los admin pueden registrar servicios");
        }
        Offering entity = mapper.toEntity(dto);
        repository.save(entity);
        return responseMapper.toDto(entity);
    }
}
