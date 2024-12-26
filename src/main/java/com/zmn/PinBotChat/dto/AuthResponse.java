package com.zmn.PinBotChat.dto;

public class AuthResponse {
    private String token;
    private String role;  // Можно вернуть роль или любую другую информацию

    public AuthResponse() {}

    public AuthResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }

    // ... геттеры/сеттеры

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}