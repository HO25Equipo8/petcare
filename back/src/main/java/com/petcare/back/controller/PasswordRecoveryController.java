package com.petcare.back.controller;

import com.petcare.back.service.PasswordRecoveryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5501", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/password")
public class PasswordRecoveryController {

    @Autowired
    private PasswordRecoveryService passwordRecoveryService;

    // 1. Request password reset
    @Operation(
            summary = "Solicitar recuperación de contraseña",
            description = "Inicia el proceso de recuperación de contraseña para el usuario con el email proporcionado. " +
                    "Si el correo está registrado, se enviará un enlace para restablecer la contraseña. " +
                    "Por seguridad, la respuesta es siempre la misma, independientemente de si el email existe o no."
    )
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        passwordRecoveryService.initiatePasswordReset(email);
        return ResponseEntity.ok("Si el mail existe, se le enviará un link para resetear la contraseña.");
    }

    // 2. Reset password
    @Operation(
            summary = "Restablecer contraseña",
            description = "Permite al usuario establecer una nueva contraseña utilizando un token de recuperación previamente enviado por correo. " +
                    "Si el token es válido y no ha expirado, la contraseña se actualiza correctamente. " +
                    "En caso contrario, se devuelve un error indicando que el token es inválido o expiró."
    )
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String token,
                                                @RequestParam String newPassword) {
        boolean success = passwordRecoveryService.resetPassword(token, newPassword);
        if (success) {
            return ResponseEntity.ok("Password has been reset successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }
}
