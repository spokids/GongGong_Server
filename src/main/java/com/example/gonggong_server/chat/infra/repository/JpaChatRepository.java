package com.example.gonggong_server.chat.infra.repository;

import com.example.gonggong_server.chat.domain.entity.Chat;
import com.example.gonggong_server.chat.domain.repository.ChatRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaChatRepository extends ChatRepository, JpaRepository<Chat, Long> {
    @Modifying
    @Query("DELETE FROM Chat c WHERE c.chatRoomId IN :chatroomIds")
    void deleteByChatroomIds(List<Long> chatroomIds);
}
