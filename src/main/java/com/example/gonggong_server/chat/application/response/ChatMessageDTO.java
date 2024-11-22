package com.example.gonggong_server.chatroom.application.response;

import java.util.List;

public record DefaultMessageDTO(String message, List<String> options) {
}
