package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.UserRegisterDTO;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.repository.UserRepository;
import com.petcare.back.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RegisterControllerTest {

    @InjectMocks
    private RegisterController registerController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LocationService locationService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(registerController, "userRepository", userRepository);
        ReflectionTestUtils.setField(registerController, "locationService", locationService);
        ReflectionTestUtils.setField(registerController, "passwordEncoder", passwordEncoder);
    }

    @Test
    void shouldRegisterNewUserSuccessfully() {
        //Arrange
        UserRegisterDTO dto = new UserRegisterDTO(
                "test@test.com",
                "Password1",
                "Password1",
                Role.USER,
                null,
                null
        );

        when(userRepository.findByEmail("test@test.com")).thenReturn(null);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("/");

        // Act
        ResponseEntity response = registerController.registerUser(dto, uriBuilder);

        // Assert
        assertEquals(201, response.getStatusCode().value());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldFailIfUserExists() {
        //Arrange
        UserRegisterDTO dto = new UserRegisterDTO(
                "test@test.com",
                "Password1",
                "Password1",
                Role.USER,
                null,
                null
        );

        when(userRepository.findByEmail("test@test.com")).thenReturn(new User());

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("/");

        // Act
        ResponseEntity response = registerController.registerUser(dto, uriBuilder);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldFailIfNoEmail() {
        //Arrange
        UserRegisterDTO dto = new UserRegisterDTO(
                null,
                "Password1",
                "Password1",
                Role.USER,
                null,
                null
        );

        when(userRepository.findByEmail(null)).thenReturn(null);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("/");

        // Act
        ResponseEntity response = registerController.registerUser(dto, uriBuilder);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldFailIfNoPassword() {
        //Arrange
        UserRegisterDTO dto = new UserRegisterDTO(
                "test@test.com",
                null,
                null,
                Role.USER,
                null,
                null
        );

        when(userRepository.findByEmail("test@test.com")).thenReturn(null);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("/");

        // Act
        ResponseEntity response = registerController.registerUser(dto, uriBuilder);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        verify(userRepository, never()).save(any(User.class));
    }



    @Test
    void shouldFailIfPasswordsDoNotMatch() {
        // Arrange
        UserRegisterDTO dto = new UserRegisterDTO(
                "test@test.com",
                "Peras",
                "Manzanas",
                Role.USER,
                null,
                null
        );

        when(userRepository.findByEmail("test@test.com")).thenReturn(null);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("/");

        // Act
        ResponseEntity response = registerController.registerUser(dto, uriBuilder);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        verify(userRepository, never()).save(any(User.class));
    }
}
