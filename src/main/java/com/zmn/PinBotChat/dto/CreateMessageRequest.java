package com.zmn.PinBotChat.dto;

import com.zmn.PinBotChat.model.MessageType;

public class CreateMessageRequest {

    private Long chatId;
    private Long senderId;
    private String content;
    private MessageType type; // TEXT, IMAGE...
    private String attachmentUrl;
    private Long replyToId; // если отвечаем на другое сообщение

    public CreateMessageRequest() {
    }

    // Геттеры/сеттеры

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public Long getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(Long replyToId) {
        this.replyToId = replyToId;
    }
}
