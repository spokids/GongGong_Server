package com.example.gonggong_server.chatroom.domain.repository;

import com.example.gonggong_server.chat.domain.entity.Chat;
import com.example.gonggong_server.chatroom.domain.entity.ChatRoom;
import com.fasterxml.jackson.annotation.OptBoolean;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {
    void deleteByUserId(Long userId);
    List<Long> findAllIdsByUserId(Long userId);
    ChatRoom save(ChatRoom chatRoom);
    Optional<ChatRoom> findTopByUserIdOrderByCreateDateDesc(Long userId);

    Optional<ChatRoom> findByChatRoomId(Long chatRoomId);
    void delete(ChatRoom chatRoom);
}
