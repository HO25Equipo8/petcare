package com.petcare.back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.back.domain.dto.request.AutocompleteSuggestion;
import com.petcare.back.domain.dto.request.LocationDTO;
import com.petcare.back.domain.dto.request.UserUpdateBackendDTO;
import com.petcare.back.domain.dto.request.UserUpdateDTO;
import com.petcare.back.domain.dto.response.NearbySitterResponseDTO;
import com.petcare.back.domain.entity.Location;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.response.NearbySitterResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.infra.error.ImageValidator;
import com.petcare.back.repository.ImageRepository;
import com.petcare.back.repository.UserRepository;
import com.petcare.back.validation.ValidationOffering;
import com.petcare.back.validation.ValidationUserProfile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final LocationService locationService;
    private final UserRepository userRepository;
    private final GeocodingService geocodingService;
    private final List<ValidationUserProfile> validations;
    private final ImageRepository imageRepository;
    private final NearbySitterResponseMapper responseMapper;

    @Transactional
    public User updateProfile(UserUpdateDTO dto) throws MyException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // ✅ Datos básicos
        user.setName(dto.name());
        user.setPhone(dto.phone());

        for (ValidationUserProfile v : validations) {
            v.validate(dto,user);
        }

        LocationDTO locationDTO = geocodingService.getLocationFromPlaceId((dto.location().placeId()));

        // ✅ Ubicación
        if (dto.location() != null) {
            Location updatedLocation;
            if (user.getLocation() != null) {
                updatedLocation = locationService.update(locationDTO, user.getLocation());
            } else {
                updatedLocation = locationService.save(locationDTO);
            }
            user.setLocation(updatedLocation);
        }

        // ✅ Roles profesionales (si aplica)
        if (user.getRole() == Role.SITTER) {
            user.setProfessionalRoles(dto.professionalRoles());
        } else {
            user.setProfessionalRoles(new ArrayList<>());
        }

        // ✅ Verificación de imágenes
        boolean tieneFotoPerfil = user.getProfilePhoto() != null;

        boolean tieneFotosVerificacion = true;
        if (user.getRole() == Role.SITTER) {
            int cantidadFotos = imageRepository.countByUserId(user.getId());
            tieneFotosVerificacion = cantidadFotos > 0;
        }

        // ✅ Estado del perfil
        boolean completo = user.getName() != null &&
                user.getPhone() != null &&
                user.getLocation() != null &&
                tieneFotoPerfil &&
                tieneFotosVerificacion;

        user.setProfileComplete(completo);
        user.setVerified(user.getRole() == Role.OWNER);

        return userRepository.save(user);
    }

    @Transactional
    public User updateProfileBackend(UserUpdateBackendDTO dto) throws MyException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // ✅ Datos básicos
        user.setName(dto.name());
        user.setPhone(dto.phone());

        AutocompleteSuggestion autocompleteSuggestion = geocodingService.getPlaceIdFromLocationDTO(dto.location());

        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(
                dto.name(),
                dto.phone(),
                autocompleteSuggestion,
                dto.professionalRoles()
        );

        for (ValidationUserProfile v : validations) {
            v.validate(userUpdateDTO,user);
        }

        // ✅ Ubicación
        if (dto.location() != null) {
            Location updatedLocation;
            if (user.getLocation() != null) {
                updatedLocation = locationService.update(dto.location(), user.getLocation());
            } else {
                updatedLocation = locationService.save(dto.location());
            }
            user.setLocation(updatedLocation);
        }

        // ✅ Roles profesionales (si aplica)
        if (user.getRole() == Role.SITTER) {
            user.setProfessionalRoles(dto.professionalRoles());
        } else {
            user.setProfessionalRoles(new ArrayList<>());
        }

        // ✅ Verificación de imágenes
        boolean tieneFotoPerfil = user.getProfilePhoto() != null;

        boolean tieneFotosVerificacion = true;
        if (user.getRole() == Role.SITTER) {
            int cantidadFotos = imageRepository.countByUserId(user.getId());
            tieneFotosVerificacion = cantidadFotos > 0;
        }

        // ✅ Estado del perfil
        boolean completo = user.getName() != null &&
                user.getPhone() != null &&
                user.getLocation() != null &&
                tieneFotoPerfil &&
                tieneFotosVerificacion;

        user.setProfileComplete(completo);
        user.setVerified(user.getRole() == Role.OWNER);

        return userRepository.save(user);
    }

    public User getPublicProfile(Long id) throws MyException {
        return userRepository.findById(id)
                .orElseThrow(() -> new MyException("Usuario no encontrado"));
    }
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }
    public List<User> findTopSitters(Pageable pageable) {
        return userRepository.findTopSittersByReputationNative(pageable);
    }

    //Método para buscar profesionales según radio
    public List<NearbySitterResponseDTO> findNearbySitters(double radiusKm) throws MyException {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Location location = owner.getLocation();

        if (location == null) {
            throw new MyException("Tu perfil no tiene una ubicación registrada.");
        }

        List<User> sitters = userRepository.findVerifiedActiveSittersWithinRadius(
                location.getLatitude(), location.getLongitude(), radiusKm
        );

        return sitters.stream()
                .map(responseMapper::toDto)
                .toList();
    }
}
