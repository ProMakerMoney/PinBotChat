package com.zmn.PinBotChat.service;

import com.zmn.PinBotChat.model.Chat;
import com.zmn.PinBotChat.model.ChatType;
import com.zmn.PinBotChat.model.User;
import com.zmn.PinBotChat.repository.ChatRepository;
import com.zmn.PinBotChat.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    // Создаём приватный чат между двумя пользователями
    public Chat createPrivateChat(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("User1 not found"));
        User user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new RuntimeException("User2 not found"));

        // Создаём набор участников
        Set<User> participants = new HashSet<>();
        participants.add(user1);
        participants.add(user2);

        Chat chat = new Chat(null, ChatType.PRIVATE, participants);
        return chatRepository.save(chat);
    }

    // Создаём групповой чат с несколькими участниками
    public Chat createGroupChat(String groupName, List<Long> userIds) {
        Set<User> participants = new HashSet<>();
        for (Long userId : userIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            participants.add(user);
        }
        Chat chat = new Chat(groupName, ChatType.GROUP, participants);
        return chatRepository.save(chat);
    }

    // Получить все чаты, где состоит userId
    public List<Chat> getChatsForUser(Long userId) {
        return chatRepository.findAllByUserId(userId);
    }

    // Добавить участника в существующий чат (групповой)
    public Chat addUserToChat(Long chatId, Long userId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        chat.getParticipants().add(user);
        return chatRepository.save(chat);
    }

    // Удалить участника из чата (если нужно)
    public Chat removeUserFromChat(Long chatId, Long userId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        chat.getParticipants().removeIf(u -> u.getId().equals(userId));
        return chatRepository.save(chat);
    }
}
