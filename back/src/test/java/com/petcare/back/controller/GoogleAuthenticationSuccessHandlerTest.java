package com.petcare.back.controller;

import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.entity.User;
import com.petcare.back.infra.gsignin.OAuth2AuthenticationSuccessHandler;
import com.petcare.back.repository.UserRepository;
import com.petcare.back.infra.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GoogleAuthenticationSuccessHandlerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2User oauth2User;

    @InjectMocks
    private OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        // Setup OAuth2User mock attributes
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", "test@gmail.com");
        attributes.put("name", "Test User");
        attributes.put("sub", "google-user-123");

        when(authentication.getPrincipal()).thenReturn(oauth2User);
        when(oauth2User.getAttribute("email")).thenReturn("test@gmail.com");
        when(oauth2User.getAttribute("name")).thenReturn("Test User");
        when(oauth2User.getAttribute("sub")).thenReturn("google-user-123");
    }

    @Test
    void testSuccessfulGoogleLoginForNewUser() throws IOException {
        // Arrange
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(null);
        when(tokenService.generateToken(any(User.class))).thenReturn("jwt-token-123");

        User savedUser = createMockUser("test@gmail.com", "Test User", "google", "google-user-123");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // Assert
        verify(userRepository).findByEmail("test@gmail.com");
        verify(userRepository).save(any(User.class));
        verify(tokenService).generateToken(any(User.class));

        assertEquals(HttpServletResponse.SC_FOUND, response.getStatus());
        assertEquals("https://petcare-zeta-kohl.vercel.app/auth-success?token=jwt-token-123",
                response.getRedirectedUrl());
    }

    @Test
    void testSuccessfulGoogleLoginForExistingGoogleUser() throws IOException {
        // Arrange - existing user already with Google provider
        User existingGoogleUser = createMockUser("test@gmail.com", "Test User", "google", "google-user-123");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(existingGoogleUser);
        when(tokenService.generateToken(existingGoogleUser)).thenReturn("jwt-token-789");

        // Act
        oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // Assert
        verify(userRepository).findByEmail("test@gmail.com");
        verify(userRepository, never()).save(any(User.class)); // Should not save again
        verify(tokenService).generateToken(existingGoogleUser);

        assertEquals(HttpServletResponse.SC_FOUND, response.getStatus());
        assertEquals("https://petcare-zeta-kohl.vercel.app/auth-success?token=jwt-token-789",
                response.getRedirectedUrl());
    }

    @Test
    void testTokenServiceIntegration() throws IOException {
        // Arrange
        User existingUser = createMockUser("test@gmail.com", "Test User", "google", "google-user-123");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(existingUser);
        when(tokenService.generateToken(existingUser)).thenReturn("integration-token");

        // Act
        oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // Assert
        verify(tokenService).generateToken(existingUser);
        assertTrue(response.getRedirectedUrl().contains("token=integration-token"));
    }

    // Helper method to create mock users
    private User createMockUser(String email, String name, String provider, String providerId) {
        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setName(name);
        user.setProvider(provider);
        user.setProviderId(providerId);
        user.setRole(Role.USER);
        user.setChecked(true);
        return user;
    }
}
