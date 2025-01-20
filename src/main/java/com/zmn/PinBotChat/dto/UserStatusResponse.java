package com.zmn.PinBotChat.dto;

import java.time.LocalDateTime;

public class UserStatusResponse {

    private boolean online; // Статус онлайн
    private LocalDateTime lastLoginDate; // Время последнего входа

    public UserStatusResponse(boolean online, LocalDateTime lastLoginDate) {
        this.online = online;
        this.lastLoginDate = lastLoginDate;
    }

    // Геттеры и сеттеры
    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}

