package com.zmn.PinBotChat.controller;

import com.zmn.PinBotChat.dto.CreateMessageRequest;
import com.zmn.PinBotChat.dto.SimplifiedMessageResponse;
import com.zmn.PinBotChat.model.Message;
import com.zmn.PinBotChat.model.MessageType;
import com.zmn.PinBotChat.service.MessageService;
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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("chatId") Long chatId,
            @RequestParam("senderId") Long senderId,
            @RequestParam(value = "type", required = false, defaultValue = "TEXT") String type,
            @RequestParam(value = "content", required = false, defaultValue = "File upload") String content,
            @RequestParam(value = "replyToId", required = false) String replyToIdStr) {
        try {
            // Обработка параметра replyToId
            Long replyToId = (replyToIdStr != null && !"null".equals(replyToIdStr)) ? Long.parseLong(replyToIdStr) : null;

            // Создаем CreateMessageRequest из параметров URL
            CreateMessageRequest request = new CreateMessageRequest();
            request.setChatId(chatId);
            request.setSenderId(senderId);
            request.setType(MessageType.valueOf(type.toUpperCase()));
            request.setContent(content);
            request.setReplyToId(replyToId);

            // Вызываем сервис
            Message message = messageService.uploadFile(file, request);

            return ResponseEntity.ok(message);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка при преобразовании replyToId: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
