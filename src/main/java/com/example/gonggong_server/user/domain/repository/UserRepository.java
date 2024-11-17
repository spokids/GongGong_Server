package com.example.gonggong_server.user.domain.repository;

import com.example.gonggong_server.user.domain.entity.User;

public interface UserRepository {
    User save(User user);
    Boolean existsByNickName(String nickName);
}