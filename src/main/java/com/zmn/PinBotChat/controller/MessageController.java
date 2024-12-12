package com.zmn.PinBotChat.controller;

import com.zmn.PinBotChat.entity.Message;
import com.zmn.PinBotChat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    // Отправка сообщения
    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    // Получение сообщений для пользователя
    @GetMapping
    public List<Message> getMessages(@RequestParam String recipient) {
        return messageRepository.findByRecipient(recipient);
    }
}
