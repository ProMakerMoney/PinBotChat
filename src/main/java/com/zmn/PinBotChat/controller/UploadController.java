package com.zmn.PinBotChat.controller;

import com.zmn.PinBotChat.dto.CreateMessageRequest;
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

@RestController
@RequestMapping("/api/messages")
public class UploadController {

    private final MessageService messageService;

    @Autowired
    public UploadController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> uploadFile(
            // Файл берём из multipart тела
            @RequestPart("file") MultipartFile file,

            // Остальные параметры — из query-параметров URL
            @RequestParam("chatId") Long chatId,
            @RequestParam("senderId") Long senderId,

            @RequestParam(value = "type", defaultValue = "TEXT") String type,
            @RequestParam(value = "content", defaultValue = "File upload") String content,
            @RequestParam(value = "replyToId", required = false) Long replyToId
    ) {
        try {
            // Формируем CreateMessageRequest (или аналог) из query-параметров
            CreateMessageRequest request = new CreateMessageRequest();
            request.setChatId(chatId);
            request.setSenderId(senderId);
            request.setType(MessageType.valueOf(type.toUpperCase()));
            request.setContent(content);
            request.setReplyToId(replyToId); // может быть null

            // Вызываем сервис
            Message message = messageService.uploadFile(file, request);
            return ResponseEntity.ok(message);

        } catch (NumberFormatException e) {
            System.out.println("Ошибка при преобразовании параметров: " + e.getMessage());
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