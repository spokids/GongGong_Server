package com.example.gonggong_server.chat.domain.repository;

import com.example.gonggong_server.chat.domain.entity.Chat;

import java.util.List;

public interface ChatRepository {
    void deleteByChatroomIds(List<Long> chatroomIds);
    Chat save(Chat chat);
    Chat findFirstByChatRoomIdOrderByCreateDateDesc(Long chatRoomId);
    List<Chat> findByChatRoomId(Long chatRoomId);
    List<Chat> findByChatRoomIdOrderByCreateDateAsc(Long chatRoomId);

    void deleteAll(List<Chat> chatsToDelete);
}
