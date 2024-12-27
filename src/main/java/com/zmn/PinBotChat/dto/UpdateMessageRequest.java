package com.zmn.PinBotChat.dto;

public class UpdateMessageRequest {
    private String content;
    private boolean deleted; // если хотим пометить как удалённое
    // и т.д.

    // геттеры/сеттеры

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
