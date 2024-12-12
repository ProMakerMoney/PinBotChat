package com.zmn.PinBotChat.controller;


import com.zmn.PinBotChat.entity.User;
import com.zmn.PinBotChat.repository.UserRepository;
import com.zmn.PinBotChat.util.JwtUtil;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        return jwtUtil.generateToken(authentication.getName());
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username is already taken";
        }
        // Хэшируем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Сохраняем пользователя
        userRepository.save(user);
        return "User registered successfully";
    }
}
