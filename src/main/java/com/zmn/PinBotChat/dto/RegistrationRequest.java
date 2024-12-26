package com.zmn.PinBotChat.dto;


public class RegistrationRequest {
    private String login;
    private String password;
    private String publicName;
    private String profilePhotoUrl; // Опционально, если хотим сразу сохранить ссылку

    // Геттеры/сеттеры/конструктор
    public RegistrationRequest() {}

    public RegistrationRequest(String login, String password, String publicName, String profilePhotoUrl) {
        this.login = login;
        this.password = password;
        this.publicName = publicName;
        this.profilePhotoUrl = profilePhotoUrl;
    }

    // ... геттеры/сеттеры


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }
}

