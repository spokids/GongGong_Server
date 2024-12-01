package com.example.gonggong_server.chat.application.response;

import lombok.Builder;

@Builder
public record ChatDTO(
        String sender,
        String chat

        ) {

}
