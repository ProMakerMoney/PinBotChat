package com.zmn.PinBotChat.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TypingService {

    private final Map<Long, Map<Long, Long>> typingStatuses = new ConcurrentHashMap<>(); // chatId -> (userId -> timestamp)

    // Обновление статуса "печатает"
    public void updateTypingStatus(Long chatId, Long userId, boolean isTyping) {
        if (isTyping) {
            typingStatuses.computeIfAbsent(chatId, k -> new ConcurrentHashMap<>()).put(userId, System.currentTimeMillis());
        } else {
            Map<Long, Long> chatTyping = typingStatuses.get(chatId);
            if (chatTyping != null) {
                chatTyping.remove(userId);
                if (chatTyping.isEmpty()) {
                    typingStatuses.remove(chatId);
                }
            }
        }
    }

    // Проверка, печатает ли пользователь
    public boolean isUserTyping(Long chatId, Long userId) {
        Map<Long, Long> chatTyping = typingStatuses.get(chatId);
        if (chatTyping != null) {
            Long timestamp = chatTyping.get(userId);
            if (timestamp != null && (System.currentTimeMillis() - timestamp) < 3000) {
                return true; // Печатает
            } else {
                chatTyping.remove(userId); // Убираем, если устарело
                if (chatTyping.isEmpty()) {
                    typingStatuses.remove(chatId);
                }
            }
        }
        return false; // Не печатает
    }

    // Сброс всех устаревших статусов
    @Scheduled(fixedRate = 3000) // Запуск каждые 3 секунды
    public void clearStaleStatuses() {
        long now = System.currentTimeMillis();
        typingStatuses.forEach((chatId, chatTyping) -> {
            chatTyping.entrySet().removeIf(entry -> (now - entry.getValue()) >= 3000);
            if (chatTyping.isEmpty()) {
                typingStatuses.remove(chatId);
            }
        });
    }
}


