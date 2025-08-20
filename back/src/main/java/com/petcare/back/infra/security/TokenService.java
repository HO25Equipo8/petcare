package com.petcare.back.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.petcare.back.domain.entity.User;
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
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withClaim("role", user.getRole().name()) // Single role
                    .withExpiresAt(generateExpiryDate())
                    .sign(algorithm); //string
        } catch (JWTCreationException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    private Instant generateExpiryDate() {
        ZoneId zone = ZoneId.systemDefault();  // Obtener la zona horaria del sistema
        return LocalDateTime.now(zone).plusMinutes(60)
                .toInstant(ZoneOffset.ofTotalSeconds(zone.getRules().getOffset(LocalDateTime.now()).getTotalSeconds()));
    }

    public String getSubject(String token) {
        if (token == null) {
            throw new RuntimeException();
        }
        DecodedJWT verifier = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            verifier = JWT.require(algorithm).withIssuer("Back").build().verify(token);
            verifier.getSubject();
        } catch (JWTVerificationException exception) {
            System.out.println(exception.getMessage());
        }
        if (verifier.getSubject() == null) {
            throw new RuntimeException("null subject from getSubject");
        }
        return verifier.getSubject();
    }

    // Extract role from token
    public String getRoleFromToken(String token) {
        if (token == null) {
            throw new RuntimeException("Token is null");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            DecodedJWT verifier = JWT.require(algorithm)
                    .withIssuer("Back")
                    .build()
                    .verify(token);

            return verifier.getClaim("role").asString();
        } catch (JWTVerificationException exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException("Invalid token", exception);
        }
    }

    // Extract user ID from token (useful for authorization)
    public Long getUserIdFromToken(String token) {
        if (token == null) {
            throw new RuntimeException("Token is null");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            DecodedJWT verifier = JWT.require(algorithm)
                    .withIssuer("Back")
                    .build()
                    .verify(token);

            return verifier.getClaim("id").asLong();
        } catch (JWTVerificationException exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException("Invalid token", exception);
        }
    }

    // Helper method to validate token and return all claims
    public DecodedJWT validateAndDecodeToken(String token) {
        if (token == null) {
            throw new RuntimeException("Token is null");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.require(algorithm)
                    .withIssuer("Back")
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException("Invalid token", exception);
        }
    }
}
