package com.guilhermeneiva.demo.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.guilhermeneiva.demo.model.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class TokenConfig {

    @Value("{jwt.secret}")
    private String secret;

    public String generateToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        String role = usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_CLIENTE");

        return JWT.create()
                .withClaim("userId", usuario.getId()) // O ID vem direto do pai!
                .withClaim("role", role)
                .withSubject(usuario.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(86400))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUserData> validToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decode = JWT.require(algorithm).build().verify(token);

            return Optional.of(JWTUserData.builder()
                    .userId(decode.getClaim("userId").asLong())
                    .email(decode.getSubject())
                    .role(decode.getClaim("role").asString())
                    .build());
        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }
    }
}