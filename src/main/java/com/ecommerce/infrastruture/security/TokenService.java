package com.ecommerce.infrastruture.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.ecommerce.domain.model.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class TokenService {

    @Value("${secret}")
    private String secret;
    public String tokenGenerate(User user) {
        try {
            return JWT.create()
                    .withIssuer("E-commerce")
                    .withSubject(user.getUsername())
                    .withExpiresAt(expireDate())
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error during token generate", exception);
        }
    }

    public String getSubject(String tokenJWT) {

        return Optional.ofNullable(JWT.require(Algorithm.HMAC256(secret))
                        .withIssuer("E-commerce")
                        .build()
                        .verify(tokenJWT)
                        .getSubject())
                .orElseThrow(() -> new RuntimeException("Token invalid or expired!"));
    }

    private Instant expireDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
