package com.zmn.PinBotChat.controller;

import com.zmn.PinBotChat.dto.CreateMessageRequest;
import com.zmn.PinBotChat.dto.SimplifiedMessageResponse;
import com.zmn.PinBotChat.model.Message;
import com.zmn.PinBotChat.model.MessageType;
import com.zmn.PinBotChat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // Создаём новое сообщение
    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody CreateMessageRequest request) {
        Message createdMessage = messageService.createMessage(request);
        return ResponseEntity.ok(createdMessage);
    }

    // Получаем все сообщения конкретного чата
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<SimplifiedMessageResponse>> getMessagesByChat(@PathVariable Long chatId) {
        List<SimplifiedMessageResponse> messages = messageService.getMessagesByChat(chatId);
        return ResponseEntity.ok(messages);
    }

    // Получаем последние 10 сообщений
    @GetMapping("/{chatId}/last")
    public ResponseEntity<List<Message>> getLastMessages(
            @PathVariable Long chatId,
            @RequestParam(defaultValue = "10") int limit) {
        List<Message> messages = messageService.getLastMessages(chatId, limit);
        return ResponseEntity.ok(messages);
    }

    // Редактирование сообщения (простой пример)
    @PutMapping("/{messageId}")
    public ResponseEntity<Message> editMessage(@PathVariable Long messageId,
                                               @RequestBody String newContent) {
        Message updated = messageService.editMessage(messageId, newContent);
        return ResponseEntity.ok(updated);
    }

    // Удаление (логическое)
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Message> deleteMessage(@PathVariable Long messageId) {
        Message deletedMsg = messageService.deleteMessage(messageId);
        return ResponseEntity.ok(deletedMsg);
    }

    // Обновить статус (например, READ)
    @PatchMapping("/{messageId}/status")
    public ResponseEntity<Message> updateStatus(@PathVariable Long messageId,
                                                @RequestParam String status) {
        // status = "DELIVERED" или "READ" и т. д.
        Message updated = messageService.updateStatus(messageId,
                Enum.valueOf(com.zmn.PinBotChat.model.MessageStatus.class, status));
        return ResponseEntity.ok(updated);
    }
}
