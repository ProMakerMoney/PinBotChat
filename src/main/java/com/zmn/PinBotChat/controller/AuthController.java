package com.zmn.PinBotChat.controller;

import com.zmn.PinBotChat.config.JwtUtil;
import com.zmn.PinBotChat.dto.AuthResponse;
import com.zmn.PinBotChat.dto.LoginRequest;
import com.zmn.PinBotChat.dto.RegistrationRequest;
import com.zmn.PinBotChat.model.User;
import com.zmn.PinBotChat.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    // Регистрация нового пользователя
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegistrationRequest request) {
        User user = authService.registerUser(request);

        // Генерация токена после регистрации
        String token = jwtUtil.generateToken(user.getLogin(), user.getRole().name());

        return ResponseEntity.ok(new AuthResponse(token, user.getRole().name(), user.getId()));
    }

    // Авторизация (логин)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        User user = authService.authenticateUser(request);

        // Генерация токена
        String token = jwtUtil.generateToken(user.getLogin(), user.getRole().name());

        return ResponseEntity.ok(new AuthResponse(token, user.getRole().name(), user.getId()));
    }
}
