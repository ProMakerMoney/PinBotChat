package com.zmn.PinBotChat.controller;

import com.zmn.PinBotChat.dto.CreateMessageRequest;
import com.zmn.PinBotChat.model.Message;
import com.zmn.PinBotChat.repository.ChatRepository;
import com.zmn.PinBotChat.repository.MessageRepository;
import com.zmn.PinBotChat.service.MessageService;
import jakarta.annotation.PostConstruct;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/ws")
public class WebSocketController {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(ChatRepository chatRepository,
                               MessageRepository messageRepository,
                               MessageService messageService,
                               SimpMessagingTemplate messagingTemplate) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/{chatId}/send")
    @SendTo("/topic/chat/{chatId}")
    public Message sendMessage(
            @DestinationVariable Long chatId,
            @Payload CreateMessageRequest incomingRequest) {

        System.out.println("Получен WS запрос: chatId=" + chatId + ", request=" + incomingRequest);

        // Устанавливаем chatId из пути
        incomingRequest.setChatId(chatId);

        // Вызываем сервис для создания сообщения
        Message createdMessage = messageService.createMessage(incomingRequest);

        // Логируем результат
        System.out.println("Создано сообщение: " + createdMessage);

        return createdMessage;
    }

    // Метод для отправки тестового сообщения при запуске сервера
    @PostConstruct
    public void sendTestMessage() {
        messagingTemplate.convertAndSend("/topic/chat/1", "Test from server");
    }
}

