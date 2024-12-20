package com.example.gonggong_server.auth.application.service;

import com.example.gonggong_server.auth.api.request.LoginRequestDTO;
import com.example.gonggong_server.auth.api.request.RegisterRequestDTO;
import com.example.gonggong_server.auth.application.response.TokenResponseDTO;
import com.example.gonggong_server.auth.exception.AuthException;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.global.jwt.JWTUtil;
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

    private static final long ACCESS_TOKEN_EXPIRATION = 10800000L; // 3시간

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        validateUserIdUniqueness(registerRequestDTO.userInputId());

        User user = User.builder()
                .userInputId(registerRequestDTO.userInputId())
                .nickName(registerRequestDTO.nickName())
                .password(passwordEncoder.encode(registerRequestDTO.password())) // 비밀번호 암호화
                .build();
        String token = jwtUtil.createJwt(user.getUserInputId(), ACCESS_TOKEN_EXPIRATION);
        userRepository.save(user);

        return TokenResponseDTO.of(token);
    }

    public TokenResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = findUserByUserInputId(loginRequestDTO.userInputId());
        validatePassword(loginRequestDTO.password(), user.getPassword());

        String token = jwtUtil.createJwt(user.getUserInputId(), ACCESS_TOKEN_EXPIRATION);

        return TokenResponseDTO.of(token);
    }

    private void validateUserIdUniqueness(String userInputId) {
        if (userRepository.existsByUserInputId(userInputId)) {
            throw new AuthException(ErrorStatus.USER_ALREADY_EXISTS);
        }
    }

    private User findUserByUserInputId(String userInputId) {
        return userRepository.findByUserInputId(userInputId)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new AuthException(ErrorStatus.PASSWORD_NOT_MATCH);
        }
    }
}