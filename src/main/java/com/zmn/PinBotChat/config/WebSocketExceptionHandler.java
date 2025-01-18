package com.zmn.PinBotChat.config;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class WebSocketExceptionHandler {

    @MessageExceptionHandler
    public void handleException(Throwable exception) {
        System.err.println("WebSocket error: " + exception.getMessage());
    }
}

