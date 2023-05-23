package com.glamreserve.glamreserve.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.glamreserve.glamreserve.entities.interceptor.ForbiddenAccessException;
import com.glamreserve.glamreserve.entities.user.*;
import com.glamreserve.glamreserve.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.glamreserve.glamreserve.entities.reserve.ReserveRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRegisterRepository userRegisterRepository;

    private ReserveRepository reserveRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserLoginRequest loginRequest, HttpServletResponse response) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(loginRequest.getUsername()));
        User user = optionalUser.orElse(null);
        if (user != null) {
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String token = AuthenticationService.generateToken(user.getUsername());
                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("token", token);
                Cookie tokenCookie = new Cookie("token", token);
                tokenCookie.setHttpOnly(true);
                tokenCookie.setPath("/");
                tokenCookie.setMaxAge(24 * 60 * 60);
                response.addCookie(tokenCookie);
                return ResponseEntity.ok(responseBody);
            } else {
                return ResponseEntity.status(403).body("Contraseña incorrecta");
            }
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest user, HttpServletResponse response) {
        try {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userRegisterRepository.save(user);
            String token = AuthenticationService.generateToken(user.getUsername());
            Map<String, String> responseBody = new HashMap<>();
            Cookie tokenCookie = new Cookie("token", token);
            tokenCookie.setHttpOnly(true);
            tokenCookie.setPath("/");
            tokenCookie.setMaxAge(24 * 60 * 60);
            response.addCookie(tokenCookie);
            responseBody.put("token", token);
            return ResponseEntity.ok(responseBody);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya existe");
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editUser(@RequestHeader("Authorization") String token,
            @ModelAttribute User user,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            String username = AuthenticationService.validateToken(token);
            User loggedInUser = userRepository.findByUsername(username);
            User originalUser = userRepository.getUserById(user.getId());

            if (loggedInUser == null || !loggedInUser.getUsername().equals(originalUser.getUsername())) {
                return ResponseEntity.badRequest().body("Acceso denegado: No eres propietario de esta cuenta.");
            }

            if (!user.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
            }else{
                user.setPassword(originalUser.getPassword());
            }

            user.setRoleId(originalUser.getRoleId());
            originalUser = user;
            User savedUser = userRepository.save(originalUser);
            String newToken = AuthenticationService.generateToken(user.getUsername());
            Cookie tokenCookie = new Cookie("token", newToken);
            tokenCookie.setHttpOnly(true);
            tokenCookie.setPath("/");
            tokenCookie.setMaxAge(24 * 60 * 60);
            response.addCookie(tokenCookie);
            return ResponseEntity.ok(savedUser);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("El email ya existe");
        }
    }



    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestBody TokenVerificationRequest tokenRequest) {
        String token = tokenRequest.getToken();

        try {
            String username = AuthenticationService.validateToken(token);
            User user = userRepository.findByUsername(username);
            return ResponseEntity.ok(user);
        } catch (JWTDecodeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expirado.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String token, @PathVariable("id") Long id, HttpServletResponse response) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User authUser = AuthenticationService.getUserFromToken(token, userRepository);
            if (!Objects.equals(user.get().getId(), authUser.getId())) {
                throw new ForbiddenAccessException("Acceso denegado: No eres propietario de esta cuenta.");
            }
            userRepository.deleteById(id);
            Cookie cookie = new Cookie("token", null);
            cookie.setMaxAge(0);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return ResponseEntity.noContent().build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
