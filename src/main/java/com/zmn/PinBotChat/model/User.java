package com.zmn.PinBotChat.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users") // Или любое название таблицы
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String login;            // Логин (уникальный)

    private String password;         // Пароль (хранить в захешированном виде)

    private String publicName;       // Публичное имя

    @Enumerated(EnumType.STRING)
    private Role role;               // Роль (ADMIN, USER)

    private String profilePhotoUrl;  // Ссылка на фото профиля

    private LocalDateTime registrationDate; // Дата регистрации

    private LocalDateTime lastLoginDate;    // Дата последнего входа

    private boolean online;          // Статус (онлайн / оффлайн)

    // Геттеры/сеттеры/конструкторы

    public User() {}

    public User(String login, String password, String publicName, Role role) {
        this.login = login;
        this.password = password;
        this.publicName = publicName;
        this.role = role;
        this.registrationDate = LocalDateTime.now();
        this.online = false;
    }

    // ... остальные геттеры и сеттеры


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}