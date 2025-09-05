package com.petcare.back.service;

import com.petcare.back.domain.entity.User;
import com.petcare.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordRecoveryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Map<String, String> tokenStorage = new ConcurrentHashMap<>(); // Simplified storage

    public void initiatePasswordReset(String email) {
        UserDetails userDetails = userRepository.findByEmail(email);
        if (userDetails != null) {
            //String token = UUID.randomUUID().toString();
            //tokenStorage.put(token, email);

            // Send email with reset link
            //String resetLink = "http://localhost:8080/password/reset?token=" + token;
            //Simplified version
            emailService.sendPassRecoverEmail(email);
        }
    }

    public boolean resetPassword(String token, String newPassword) {
        return false;
    }
}
