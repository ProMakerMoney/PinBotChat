package com.zmn.PinBotChat.service;

import com.zmn.PinBotChat.dto.LoginRequest;
import com.zmn.PinBotChat.dto.RegistrationRequest;
import com.zmn.PinBotChat.model.Role;
import com.zmn.PinBotChat.model.User;
import com.zmn.PinBotChat.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegistrationRequest request) {
        // Проверяем, не занят ли login
        userRepository.findByLogin(request.getLogin()).ifPresent(u -> {
            throw new RuntimeException("Такой логин уже существует");
        });

        User user = new User(
                request.getLogin(),
                passwordEncoder.encode(request.getPassword()), // хешируем пароль
                request.getPublicName(),
                Role.USER
        );

        // Если нужно сразу сохранять фото
        if (request.getProfilePhotoUrl() != null) {
            user.setProfilePhotoUrl(request.getProfilePhotoUrl());
        }

        user.setRegistrationDate(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User authenticateUser(LoginRequest request) {
        User user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        // Обновим дату входа
        user.setLastLoginDate(LocalDateTime.now());
        user.setOnline(true); // ставим статус "онлайн", если хотим
        userRepository.save(user);

        return user;
    }
}