package com.petcare.back.service;

import com.petcare.back.domain.dto.request.*;
import com.petcare.back.domain.dto.response.NearbySitterResponseDTO;
import com.petcare.back.domain.dto.response.PetResponseDTO;
import com.petcare.back.domain.dto.response.UserUpdateResponseDTO;
import com.petcare.back.domain.entity.Location;
import com.petcare.back.domain.entity.Pet;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.response.NearbySitterResponseMapper;
import com.petcare.back.domain.mapper.response.PetResponseMapper;
import com.petcare.back.domain.mapper.response.UserUpdateResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.FeedbackRepository;
import com.petcare.back.repository.ImageRepository;
import com.petcare.back.repository.PetRepository;
import com.petcare.back.repository.UserRepository;
import com.petcare.back.validation.ValidationUserProfile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
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
    private final UserUpdateResponseMapper userUpdateResponseMapper;
    private final FeedbackRepository feedbackRepository;
    private final PetRepository petRepository;
    private final PetResponseMapper petResponseMapper;

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
        user.setChecked(user.getRole() == Role.OWNER);

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
        user.setChecked(user.getRole() != Role.SITTER);

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

    //buscar profesionales según radio
    public List<NearbySitterResponseDTO> findNearbySitters(double radiusKm) throws MyException {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Location location = owner.getLocation();

        if (location == null) {
            throw new MyException("Tu perfil no tiene una ubicación registrada.");
        }

        List<User> sitters = userRepository.findCheckedActiveSittersWithinRadius(
                location.getLatitude(), location.getLongitude(), radiusKm
        );

        return sitters.stream()
                .map(responseMapper::toDto)
                .toList();
    }

    public List<UserPublicProfileDTO> getAllPublicProfiles() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toPublicProfileDTO)
                .toList();
    }

    public UserUpdateResponseDTO getUserProfileById(Long id) throws MyException {
        return userRepository.findById(id)
                .map(userUpdateResponseMapper::toDTO)
                .orElseThrow(() -> new MyException("Usuario no encontrado"));
    }

    public UserPublicProfileDTO toPublicProfileDTO(User user) {
        String locationLabel;

        if (user.getLocation() != null) {
            Location loc = user.getLocation();
            locationLabel = String.format("%s %s, %s, %s",
                    loc.getStreet() != null ? loc.getStreet() : "",
                    loc.getNumber() != null ? loc.getNumber() : "",
                    loc.getCity() != null ? loc.getCity() : "",
                    loc.getProvince() != null ? loc.getProvince() : "").trim();
        } else {
            locationLabel = "Ubicación no especificada";
        }

        String photoUrl = (user.getProfilePhoto() != null)
                ? user.getProfilePhoto().getImageName()
                : null;

        List<ProfessionalRoleEnum> roles = (user.getProfessionalRoles() != null)
                ? user.getProfessionalRoles()
                : List.of();

        Integer feedbackCount = feedbackRepository.getFeedbackCountForUser(user.getId());

        return new UserPublicProfileDTO(
                user.getName(),
                locationLabel,
                photoUrl,
                roles,
                feedbackCount
        );
    }

    public Page<PetResponseDTO> findPetsOfAuthenticatedOwner(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("Solo los OWNER pueden consultar sus mascotas");
        }

        Page<Pet> petsPage = petRepository.findAllByOwnerId(user.getId(), pageable);

        return petsPage.map(p -> petResponseMapper.toDto(p));
    }

    public List<User> getAllSitters() {
        return userRepository.findByRole(Role.SITTER);
    }

    public List<User> getActiveSitters() {
        return userRepository.findByRoleAndActive(Role.SITTER, true);
    }

    public List<User> getAllOwners() {
        return userRepository.findByRole(Role.OWNER);
    }

    public List<User> getActiveOwners() {
        return userRepository.findByRoleAndActive(Role.OWNER, true);
    }

    @Transactional
    public User activateUser(Long userId) throws MyException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new MyException("Usuario no encontrado"));

        user.setActive(true);
        return userRepository.save(user);
    }

    @Transactional
    public User deactivateUser(Long userId) throws MyException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new MyException("Usuario no encontrado"));

        user.setActive(false);
        return userRepository.save(user);
    }
}
