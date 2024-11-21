package com.example.gonggong_server.user.infra.repository;

import com.example.gonggong_server.user.domain.entity.User;
import com.example.gonggong_server.user.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.userId IN :userIds")
    List<User> findAllByUserId(List<Long> userIds);
}
