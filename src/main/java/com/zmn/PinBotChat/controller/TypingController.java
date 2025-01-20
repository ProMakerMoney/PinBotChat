package com.zmn.PinBotChat.controller;

import com.zmn.PinBotChat.service.TypingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/typing")
public class TypingController {

    private final TypingService typingService;

    public TypingController(TypingService typingService) {
        this.typingService = typingService;
    }

    // Получение статуса "печатает"
    @GetMapping("/status")
    public ResponseEntity<Boolean> getTypingStatus(@RequestParam Long chatId, @RequestParam Long userId) {
        boolean isTyping = typingService.isUserTyping(chatId, userId);
        return ResponseEntity.ok(isTyping);
    }

    // Обновление статуса "печатает"
    @PostMapping("/notify")
    public ResponseEntity<Void> notifyTyping(@RequestParam Long chatId, @RequestParam Long userId, @RequestParam boolean isTyping) {
        typingService.updateTypingStatus(chatId, userId, isTyping);
        return ResponseEntity.ok().build();
    }
}
