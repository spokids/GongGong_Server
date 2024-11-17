package com.example.gonggong_server.user.domain.repository;

import com.example.gonggong_server.user.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Boolean existsByUserId(String userId);
    Optional<User> findByUserId(String userId);
}