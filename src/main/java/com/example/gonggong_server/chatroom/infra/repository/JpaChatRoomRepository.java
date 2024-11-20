package com.example.gonggong_server.chatroom.infra.repository;

import com.example.gonggong_server.chatroom.domain.entity.ChatRoom;
import com.example.gonggong_server.chatroom.domain.repository.ChatRoomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaChatRoomRepository extends ChatRoomRepository, JpaRepository<ChatRoom, Long> {
    @Query("SELECT c.chatRoomId FROM ChatRoom c WHERE c.userId = :userId")
    List<Long> findAllIdsByUserId(Long userId);
}
