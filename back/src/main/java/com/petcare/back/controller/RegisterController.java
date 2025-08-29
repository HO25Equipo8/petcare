package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.LocationDTO;
import com.petcare.back.domain.dto.request.UserRegisterDTO;
import com.petcare.back.domain.dto.response.UserDTO;
import com.petcare.back.domain.entity.Location;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.repository.UserRepository;
import com.petcare.back.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationService locationService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity registerUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO
            , UriComponentsBuilder uriComponentsBuilder){
        // Check if email already exists
        if (userRepository.findByEmail(userRegisterDTO.login()) != null) {
            return ResponseEntity.badRequest().build();
        }

        // Check if passwords match
        if (!userRegisterDTO.pass1().equals(userRegisterDTO.pass2())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        Role role;
        if (userRegisterDTO.role() != null) {
            role = userRegisterDTO.role();
        } else {
            role = Role.USER;
        }

        String encryptedPassword = passwordEncoder.encode(userRegisterDTO.pass1());
        User newUser = new User(userRegisterDTO.login(), encryptedPassword, role);

        userRepository.save(newUser);

        // return ResponseEntity.ok().build();
        UserDTO userDTO = new UserDTO(newUser.getId());
        URI url = uriComponentsBuilder.path("/users/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(url).body(userDTO);
    }
}
