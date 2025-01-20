package com.zmn.PinBotChat.controller;

import com.zmn.PinBotChat.config.JwtUtil;
import com.zmn.PinBotChat.dto.CreateMessageRequest;
import com.zmn.PinBotChat.dto.SimplifiedMessageResponse;
import com.zmn.PinBotChat.model.Message;
import com.zmn.PinBotChat.service.MessageService;
import com.zmn.PinBotChat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public MessageController(MessageService messageService, JwtUtil jwtUtil, UserService userService) {
        this.messageService = messageService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Убираем "Bearer "
        }
        return null;
    }

    private String getUsernameFromRequest(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Invalid or missing token");
        }
        return jwtUtil.getUsernameFromToken(token);
    }

    private void setUserOnline(HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        if (username != null) {
            userService.updateUserOnlineStatus(username);
        }
    }

    // Создаём новое сообщение
    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody CreateMessageRequest request, HttpServletRequest httpRequest) {
        setUserOnline(httpRequest); // Обновляем статус онлайн

        Message createdMessage = messageService.createMessage(request);
        return ResponseEntity.ok(createdMessage);
    }

    // Получаем все сообщения конкретного чата
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<SimplifiedMessageResponse>> getMessagesByChat(
            @PathVariable Long chatId, HttpServletRequest request) {
        setUserOnline(request); // Обновляем статус онлайн

        List<SimplifiedMessageResponse> messages = messageService.getMessagesByChat(chatId);
        return ResponseEntity.ok(messages);
    }

    // Получаем последние 10 сообщений
    @GetMapping("/{chatId}/last")
    public ResponseEntity<List<Message>> getLastMessages(
            @PathVariable Long chatId,
            @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest httpRequest) {
        setUserOnline(httpRequest); // Обновляем статус онлайн

        List<Message> messages = messageService.getLastMessages(chatId, limit);
        return ResponseEntity.ok(messages);
    }

    // Редактирование сообщения (простой пример)
    @PutMapping("/{messageId}")
    public ResponseEntity<Message> editMessage(@PathVariable Long messageId,
                                               @RequestBody String newContent,
                                               HttpServletRequest httpRequest) {
        setUserOnline(httpRequest); // Обновляем статус онлайн

        Message updated = messageService.editMessage(messageId, newContent);
        return ResponseEntity.ok(updated);
    }

    // Удаление (логическое)
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Message> deleteMessage(@PathVariable Long messageId, HttpServletRequest httpRequest) {
        setUserOnline(httpRequest); // Обновляем статус онлайн

        Message deletedMsg = messageService.deleteMessage(messageId);
        return ResponseEntity.ok(deletedMsg);
    }

    // Обновить статус (например, READ)
    @PatchMapping("/{messageId}/status")
    public ResponseEntity<Message> updateStatus(@PathVariable Long messageId,
                                                @RequestParam String status,
                                                HttpServletRequest httpRequest) {
        setUserOnline(httpRequest); // Обновляем статус онлайн

        Message updated = messageService.updateStatus(messageId,
                Enum.valueOf(com.zmn.PinBotChat.model.MessageStatus.class, status));
        return ResponseEntity.ok(updated);
    }
}

