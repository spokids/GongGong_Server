package com.example.gonggong_server.chat.application.response;

import java.util.List;

public record ChatMessageDTO(String message, List<String> options) {
}
