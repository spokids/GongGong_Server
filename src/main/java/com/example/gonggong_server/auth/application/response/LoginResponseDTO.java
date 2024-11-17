package com.example.gonggong_server.auth.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;

    public static LoginResponseDTO of(String token) {
        return LoginResponseDTO.builder()
                .token(token)
                .build();
    }
}
