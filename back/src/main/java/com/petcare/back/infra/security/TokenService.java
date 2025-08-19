package com.petcare.back.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.infra.security.secret}")
    private String apiSecret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret); //secreto para validar firma
            return JWT.create().withIssuer("Back")
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId())
                    .withClaim("role", usuario.getRol().name()) // Single role
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm); //creo un string
        } catch (JWTCreationException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

}
