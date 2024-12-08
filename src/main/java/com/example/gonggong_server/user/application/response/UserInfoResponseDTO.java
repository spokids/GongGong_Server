package com.example.gonggong_server.user.application.response;

public record UserInfoResponseDTO(
        String nickName,
        String userInputId,
        int reviewCount
) {
}
