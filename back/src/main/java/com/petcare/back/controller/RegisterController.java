package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.UserRegisterDTO;
import com.petcare.back.domain.dto.response.UserDTO;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.repository.UserRepository;
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
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity registerUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO
            , UriComponentsBuilder uriComponentsBuilder){
        if (userRepository.findByEmail(userRegisterDTO.login()) != null) {
            return ResponseEntity.badRequest().build();
        }

        Role role;
        if (userRegisterDTO.role() != null) {
            role = userRegisterDTO.role();
        } else {
            role = Role.USER;
        }

        String encryptedPassword = passwordEncoder.encode(userRegisterDTO.pass());
        User newUser = new User(userRegisterDTO.login(), encryptedPassword, role);
        userRepository.save(newUser);

        // return ResponseEntity.ok().build();
        UserDTO userDTO = new UserDTO(newUser.getId());
        URI url = uriComponentsBuilder.path("/users/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(url).body(userDTO);
    }
}
