package com.zmn.PinBotChat.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class CustomChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        try {
            // Проверка команды STOMP
            StompCommand command = accessor.getCommand();
            if (command == null) {
                throw new IllegalArgumentException("Invalid STOMP command");
            }
        } catch (Exception e) {
            System.err.println("Malformed STOMP packet: " + e.getMessage());
            return null; // Игнорируем некорректное сообщение
        }

        return message;
    }
}
