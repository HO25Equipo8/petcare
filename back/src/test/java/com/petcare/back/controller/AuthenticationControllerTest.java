package com.petcare.back.controller;

import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.entity.User;
import com.petcare.back.infra.security.TokenService;
import com.petcare.back.domain.dto.request.UserAuthenticationDTO;
import com.petcare.back.infra.security.DataJwToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {
    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() {
        // Arrange
        UserAuthenticationDTO dto = new UserAuthenticationDTO("test@test.com", "Password1");
        User fakeUser = new User("test@test.com", "encryptedPass", Role.USER, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(fakeUser);
        when(tokenService.generateToken(fakeUser)).thenReturn("fake-jwt-token");

        // Act
        ResponseEntity response = authenticationController.authenticateUser(dto);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        DataJwToken token = (DataJwToken) response.getBody();
        assertNotNull(token);
        assertEquals("fake-jwt-token", token.jwToken());
    }

    @Test
    void shouldThrowExceptionWhenAuthenticationFails() {
        // Arrange
        UserAuthenticationDTO dto = new UserAuthenticationDTO("wrong@test.com", "badPass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authenticationController.authenticateUser(dto));
        verify(tokenService, never()).generateToken(any(User.class));
    }
}
