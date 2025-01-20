package com.zmn.PinBotChat.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Component
public class OnlineStatusScheduler {

    private final UserService userService;

    public OnlineStatusScheduler(UserService userService) {
        this.userService = userService;
    }

    /**
     * Запускает проверку и сброс статуса онлайн каждые 5 секунд.
     */
    @Scheduled(fixedRate = 5000) // Запуск каждые 5 секунд
    public void resetOfflineStatuses() {
        userService.resetOfflineStatus();
        System.out.println("Сброшены статусы онлайн для неактивных пользователей.");
    }
}

