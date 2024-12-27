package com.zmn.PinBotChat.repository;

import com.zmn.PinBotChat.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Найти все сообщения конкретного чата, отсортированные по времени
    List<Message> findByChatIdOrderByTimestampAsc(Long chatId);

    // Найти сообщения с пагинацией (для загрузки ограниченного числа сообщений)
    Page<Message> findByChatId(Long chatId, Pageable pageable);

    // Опционально: найти последние N сообщений в чате (например, для первого экрана)
    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId ORDER BY m.timestamp DESC")
    List<Message> findTopNMessagesByChatId(@Param("chatId") Long chatId, Pageable pageable);
}

