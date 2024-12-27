package com.zmn.PinBotChat.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // В каком чате сообщение
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    // Кто отправил
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(columnDefinition = "TEXT")
    private String content;

    // Статус (SENT, DELIVERED, READ)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    // Дата/время отправки/создания
    private LocalDateTime timestamp;

    // Если реализуем «ответ на сообщение» (reply)
    @ManyToOne
    @JoinColumn(name = "reply_to_id")
    private Message replyTo;

    // Было ли сообщение отредактировано
    private boolean edited;

    // Когда отредактировано
    private LocalDateTime editedAt;

    // Было ли сообщение удалено (логическое удаление)
    private boolean deleted;

    // Тип сообщения (текст, изображение и т.д.)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    // Если в сообщении есть вложение (например, URL на файл)
    private String attachmentUrl;

    // Конструкторы
    public Message() {
    }

    // Если нужно — можно добавить конструктор с аргументами

    // Геттеры/сеттеры

    public Long getId() {
        return id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Message getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Message replyTo) {
        this.replyTo = replyTo;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
}
