package com.example.gonggong_server.option.domain.repository;

import com.example.gonggong_server.option.domain.entity.Option;

import java.util.List;

public interface OptionRepository {
    List<Option> findByChatRoomId(Long chatId);
    Option save(Option option);

}
