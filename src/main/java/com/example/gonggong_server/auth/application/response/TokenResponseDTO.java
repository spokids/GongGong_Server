package com.example.gonggong_server.auth.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDTO {
    private String token;

    public static TokenResponseDTO of(String token) {
        return TokenResponseDTO.builder()
                .token(token)
                .build();
    }
}
