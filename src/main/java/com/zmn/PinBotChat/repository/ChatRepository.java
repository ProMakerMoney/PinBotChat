package com.zmn.PinBotChat.repository;

import com.zmn.PinBotChat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    // Часто пишут метод для поиска всех чатов конкретного пользователя
    // Но при ManyToMany удобнее писать кастомный запрос:

    // Пример JPQL: "SELECT c FROM Chat c JOIN c.participants p WHERE p.id = :userId"
    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE p.id = :userId")
    List<Chat> findAllByUserId(@Param("userId") Long userId);
}
