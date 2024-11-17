package com.example.gonggong_server.auth.application.service;

import com.example.gonggong_server.auth.api.request.RegisterRequestDTO;
import com.example.gonggong_server.auth.exception.AuthException;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.user.domain.entity.User;
import com.example.gonggong_server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;


    public void register(RegisterRequestDTO registerRequestDTO) {
        String userId = registerRequestDTO.userId();
        String nickName = registerRequestDTO.nickName();
        String password = registerRequestDTO.password();

        Boolean isExist = userRepository.existsByUserId(userId);

        if (isExist) {
            throw new AuthException(ErrorStatus.USER_EXISTS);
        }
        User user = User.builder()
                .userId(userId)
                .nickName(nickName)
                .password(password)
                .build();

        userRepository.save(user);
    }
}