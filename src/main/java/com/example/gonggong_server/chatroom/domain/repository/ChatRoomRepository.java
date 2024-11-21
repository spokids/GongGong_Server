package com.example.gonggong_server.chatroom.domain.repository;

import java.util.List;

public interface ChatRoomRepository {
    void deleteByUserId(Long userId);
    List<Long> findAllIdsByUserId(Long userId);
}
