package com.zmn.PinBotChat.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // например, название группы. Для приватного чата может быть null

    @Enumerated(EnumType.STRING)
    private ChatType type; // PRIVATE, GROUP, CHANNEL...

    // Удобный способ хранения участников — @ManyToMany
    @ManyToMany
    @JoinTable(
            name = "chat_users",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants = new HashSet<>();

    private LocalDateTime createdAt;

    // Конструкторы, геттеры/сеттеры
    public Chat() {
    }

    public Chat(String name, ChatType type, Set<User> participants) {
        this.name = name;
        this.type = type;
        this.participants = participants;
        this.createdAt = LocalDateTime.now();
    }

    // ... остальные геттеры/сеттеры


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatType getType() {
        return type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
