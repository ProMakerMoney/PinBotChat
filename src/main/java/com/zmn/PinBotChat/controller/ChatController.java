package com.zmn.PinBotChat.controller;

import com.zmn.PinBotChat.model.Chat;
import com.zmn.PinBotChat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Создание приватного чата
    @PostMapping("/private")
    public ResponseEntity<Chat> createPrivateChat(@RequestParam Long userId1,
                                                  @RequestParam Long userId2) {
        Chat chat = chatService.createPrivateChat(userId1, userId2);
        return ResponseEntity.ok(chat);
    }

    // Создание группового чата
    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupChat(@RequestParam String name,
                                                @RequestBody List<Long> userIds) {
        Chat chat = chatService.createGroupChat(name, userIds);
        return ResponseEntity.ok(chat);
    }

    // Получаем все чаты текущего пользователя
    @GetMapping
    public ResponseEntity<List<Chat>> getChatsForUser(@RequestParam Long userId) {
        List<Chat> chats = chatService.getChatsForUser(userId);
        return ResponseEntity.ok(chats);
    }

    // Добавляем пользователя в чат
    @PostMapping("/{chatId}/addUser")
    public ResponseEntity<Chat> addUserToChat(@PathVariable Long chatId,
                                              @RequestParam Long userId) {
        Chat updated = chatService.addUserToChat(chatId, userId);
        return ResponseEntity.ok(updated);
    }

    // Удаляем пользователя из чата
    @DeleteMapping("/{chatId}/removeUser")
    public ResponseEntity<Chat> removeUserFromChat(@PathVariable Long chatId,
                                                   @RequestParam Long userId) {
        Chat updated = chatService.removeUserFromChat(chatId, userId);
        return ResponseEntity.ok(updated);
    }
}
