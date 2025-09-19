package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.UserAuthenticationDTO;
import com.petcare.back.domain.entity.User;
import com.petcare.back.infra.security.DataJwToken;
import com.petcare.back.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173/", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Operation(
            summary = "Autenticar usuario",
            description = "Recibe las credenciales de un usuario (email y contraseña), valida su autenticación y devuelve un token JWT si es exitoso. " +
                    "Este token debe ser utilizado en las siguientes solicitudes para acceder a recursos protegidos."
    )
    @PostMapping
    public ResponseEntity authenticateUser(@RequestBody @Valid UserAuthenticationDTO userAuthenticationDTO)
    {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userAuthenticationDTO.email(), userAuthenticationDTO.pass()
        );
        var authenticatedUser = authenticationManager.authenticate(authToken);
        var jwToken = tokenService.generateToken((User) authenticatedUser.getPrincipal());
        return ResponseEntity.ok(new DataJwToken(jwToken));
    }
}
