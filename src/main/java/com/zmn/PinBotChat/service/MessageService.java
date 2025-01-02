package com.zmn.PinBotChat.service;

import com.zmn.PinBotChat.dto.CreateMessageRequest;
import com.zmn.PinBotChat.dto.SimplifiedMessageResponse;
import com.zmn.PinBotChat.model.*;
import com.zmn.PinBotChat.repository.ChatRepository;
import com.zmn.PinBotChat.repository.MessageRepository;
import com.zmn.PinBotChat.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final String UPLOAD_DIR = "uploads/";

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository,
                          ChatRepository chatRepository,
                          UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    // Создаём новое сообщение
    public Message createMessage(CreateMessageRequest req) {
        Chat chat = chatRepository.findById(req.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat not found: " + req.getChatId()));
        User sender = userRepository.findById(req.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found: " + req.getSenderId()));

        // Проверяем, состоит ли отправитель в этом чате (если бизнес-логика требует)
        if (!chat.getParticipants().contains(sender)) {
            throw new RuntimeException("User is not in this chat.");
        }

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(req.getContent());
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);
        message.setType(req.getType() != null ? req.getType() : MessageType.TEXT);
        message.setAttachmentUrl(req.getAttachmentUrl());

        if (req.getReplyToId() != null) {
            Message replyTo = messageRepository.findById(req.getReplyToId())
                    .orElseThrow(() -> new RuntimeException("Message to reply not found: " + req.getReplyToId()));
            message.setReplyTo(replyTo);
        }

        return messageRepository.save(message);
    }

    // Получить все сообщения чата (или с пагинацией)
    public List<SimplifiedMessageResponse> getMessagesByChat(Long chatId) {
        List<Message> messages = messageRepository.findByChatIdOrderByTimestampAsc(chatId);
        return messages.stream()
                .map(message -> new SimplifiedMessageResponse(
                        message.getId(),
                        message.getChat().getId(),
                        message.getSender().getId(),
                        message.getContent(),
                        message.getStatus().name(),
                        message.getTimestamp().toString(),
                        message.getAttachmentUrl()
                ))
                .collect(Collectors.toList());
    }

    // Редактировать сообщение
    public Message editMessage(Long messageId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found: " + messageId));

        message.setContent(newContent);
        message.setEdited(true);
        message.setEditedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    // Логическое удаление сообщения
    public Message deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found: " + messageId));

        message.setDeleted(true);
        message.setContent("Message deleted"); // если хотим затирать контент
        return messageRepository.save(message);
    }

    // Обновление статуса на DELIVERED или READ
    public Message updateStatus(Long messageId, MessageStatus newStatus) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found: " + messageId));

        message.setStatus(newStatus);
        return messageRepository.save(message);
    }



    public String saveFile(MultipartFile file, Long chatId, MessageType type) throws IOException {
        // Определяем подпапку по типу
        String subfolder = getSubfolderByType(type);

        // Генерируем имя файла
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Формируем путь: uploads/<chatId>/<subfolder>/
        Path uploadPath = Paths.get(UPLOAD_DIR, String.valueOf(chatId), subfolder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Полный путь к файлу
        Path filePath = uploadPath.resolve(fileName);

        // Копируем содержимое
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Формируем "URL" или относительный путь для attachmentUrl
        // Например: "/api/uploads/chatId/subfolder/fileName"
        // Или, если вы хотите общий REST-эндпоинт, можно так:
        String url = String.format("%d/%s/%s", chatId, subfolder, fileName);

        return url;
    }

    public Message uploadFile(MultipartFile file, CreateMessageRequest req) throws IOException {

        System.out.println("----- Начало загрузки файла -----");
        System.out.println("Чат ID: " + req.getChatId());
        System.out.println("Отправитель ID: " + req.getSenderId());
        System.out.println("Тип сообщения: " + req.getType());
        System.out.println("Сохранение файла...");

        // Сохраняем файл с учётом структуры
        String fileUrl = saveFile(file, req.getChatId(), req.getType());

        Chat chat = chatRepository.findById(req.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat not found: " + req.getChatId()));
        User sender = userRepository.findById(req.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found: " + req.getSenderId()));

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        // Если хотите вывести что-то осмысленное, например "Прикреплён файл ..."
        message.setContent(req.getContent());
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);
        message.setType(req.getType() != null ? req.getType() : MessageType.TEXT);
        message.setAttachmentUrl(fileUrl);

        return messageRepository.save(message);
    }

    public Message saveMessage(Message message) {
        // Реализация сохранения сообщения в базу
        return messageRepository.save(message);
    }

    private String getSubfolderByType(MessageType type) {
        if (type == null) {
            return "other";
        }
        switch (type) {
            case IMAGE:
                return "images";
            case VIDEO:
                return "videos";
            case AUDIO:
                return "audio";
            default:
                return "other"; // или "files"
        }
    }
}
