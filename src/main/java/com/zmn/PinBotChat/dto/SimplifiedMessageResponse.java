package com.zmn.PinBotChat.dto;

public class SimplifiedMessageResponse {
    private Long id;
    private Long chatId;
    private Long senderId;
    private String content;
    private String status;
    private String timestamp;
    private String attachmentUrl; // <-- добавляем поле для ссылки на вложение

    // Конструктор со всеми параметрами
    public SimplifiedMessageResponse(Long id, Long chatId, Long senderId, String content, String status, String timestamp, String attachmentUrl) {
        this.id = id;
        this.chatId = chatId;
        this.senderId = senderId;
        this.content = content;
        this.status = status;
        this.timestamp = timestamp;
        this.attachmentUrl = attachmentUrl;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }
}


