package com.petcare.back.infra.gsignin;

import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.infra.security.TokenService;
import com.petcare.back.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String googleId = oauth2User.getAttribute("sub");

        String frontUrl = "https://petcare-zeta-kohl.vercel.app";

        // Find or create user
        UserDetails userDetails = userRepository.findByEmail(email);
        User user;

        if (userDetails == null) {
            // User doesn't exist, create new Google user
            user = createNewGoogleUser(email, name, googleId);
        } else {
            // Cast UserDetails to User
            user = (User) userDetails;
            // User exists, update provider info if needed
            if (user.getProvider() == null || !user.getProvider().equals("google")) {
                user.setProvider("google");
                user.setProviderId(googleId);
                userRepository.save(user);
            }
        }

        // Generate JWT token
        String token = tokenService.generateToken(user);

        // Redirect to frontend with token
        String redirectUrl = frontUrl + "/auth-success?token=" + token;
        response.sendRedirect(redirectUrl);
    }

    private User createNewGoogleUser(String email, String name, String googleId) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setProvider("google");
        user.setProviderId(googleId);
        user.setVerified(true); // Google emails are verified
        user.setRole(Role.USER); // Default role
        // Password is null for OAuth users

        return userRepository.save(user);
    }
}
