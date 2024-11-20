package com.example.gonggong_server.chat.domain.repository;

import java.util.List;

public interface ChatRepository {
    void deleteByChatroomIds(List<Long> chatroomIds);
}
