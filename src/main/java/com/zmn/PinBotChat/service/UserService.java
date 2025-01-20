package com.zmn.PinBotChat.service;

import com.zmn.PinBotChat.dto.UserStatusResponse;
import com.zmn.PinBotChat.model.User;
import com.zmn.PinBotChat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateUserOnlineStatus(String username) {
        userRepository.findByLogin(username).ifPresent(user -> {
            user.setLastLoginDate(LocalDateTime.now());
            user.setOnline(true);
            userRepository.save(user);
        });
    }

    public void markUserOffline(String username) {
        userRepository.findByLogin(username).ifPresent(user -> {
            user.setOnline(false);
            userRepository.save(user);
        });
    }

    public void resetOfflineStatus() {
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(15);
        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.isOnline() && user.getLastLoginDate().isBefore(threshold))
                .toList();

        for (User user : users) {
            user.setOnline(false);
        }
        userRepository.saveAll(users);
    }

    public UserStatusResponse getUserStatus(String username) {
        return userRepository.findByLogin(username)
                .map(user -> new UserStatusResponse(user.isOnline(), user.getLastLoginDate()))
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    }

    public UserStatusResponse getUserStatusById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> new UserStatusResponse(
                        user.isOnline(),
                        user.getLastLoginDate()
                ))
                .orElse(null);
    }
}
