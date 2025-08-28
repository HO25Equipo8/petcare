package com.petcare.back.controller;

import com.petcare.back.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://127.0.0.1:5501", allowedHeaders = "*", allowCredentials = "true")
@SecurityRequirement(name = "bearer-key")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocationController {

    private final UserRepository userRepository;
    @GetMapping("/users-with-location")
    public List<Map<String, Object>> getUsersWithLocation() {
        return userRepository.findAll().stream()
                .filter(user -> user.getLocation() != null)
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("email", user.getEmail());
                    map.put("latitude", user.getLocation().getLatitude());
                    map.put("longitude", user.getLocation().getLongitude());
                    return map;
                })
                .collect(Collectors.toList());
    }
}