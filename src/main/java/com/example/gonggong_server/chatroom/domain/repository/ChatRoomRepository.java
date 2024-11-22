package com.example.gonggong_server.chatroom.domain.repository;

import com.example.gonggong_server.chat.domain.entity.Chat;
import com.example.gonggong_server.chatroom.domain.entity.ChatRoom;

import java.util.List;

public interface ChatRoomRepository {
    void deleteByUserId(Long userId);
    List<Long> findAllIdsByUserId(Long userId);

    ChatRoom save(ChatRoom chatRoom);
}
