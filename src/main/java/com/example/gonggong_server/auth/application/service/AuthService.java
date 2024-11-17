package com.example.gonggong_server.auth.application.service;

import com.example.gonggong_server.auth.api.request.JoinDTO;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public void register(JoinDTO joinDTO) {

        String nickName = joinDTO.nickName();
        String password = joinDTO.password();

        Boolean isExist = userRepository.existsByNickName(nickName);

        if (isExist) {
            throw new AuthException(ErrorStatus.NICKNAME_EXISTS);
        }
        User user = User.builder()
                .nickName(nickName)
                .password(password)
                .build();

        userRepository.save(user);
    }
}