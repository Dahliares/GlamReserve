package com.glamreserve.glamreserve.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.glamreserve.glamreserve.entities.interceptor.InvalidTokenException;
import com.glamreserve.glamreserve.entities.interceptor.UserNotFoundException;
import com.glamreserve.glamreserve.entities.user.User;
import com.glamreserve.glamreserve.entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class AuthenticationService {

    public static String generateToken(String username) {
        String secret = System.getenv("TOKEN_SECRET");
        Date expiration = new Date(System.currentTimeMillis() + 3600000*24); // Token valid for 24 hours

        Algorithm algorithm = Algorithm.HMAC512(secret);
        String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(expiration)
                .sign(algorithm);
        return token;
    }

    public static String validateToken(String token) {
        String username = null;
        String secret = System.getenv("TOKEN_SECRET");

        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            JWTVerifier verifier = JWT.require(algorithm).build(); // Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            username = jwt.getSubject();
        } catch (JWTVerificationException exception){
            // Invalid token
        }

        return username;
    }

    public static User getUserFromToken(String token, UserRepository userRepository) {
        if (token == null || token.isEmpty()) {
            throw new InvalidTokenException("No se ha pasado un token.");
        }

        String username = AuthenticationService.validateToken(token);

        if (username == null || username.isEmpty()) {
            throw new InvalidTokenException("Token inválido o expirado");
        }

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UserNotFoundException("Usuario no encontrado");
        }
        return user;
    }

}
