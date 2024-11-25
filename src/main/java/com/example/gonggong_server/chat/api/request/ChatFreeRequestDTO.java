package com.example.gonggong_server.chat.api.request;


import java.util.Map;

public record ChatFreeRequestDTO(Long chatRoomId, String userFreeInput) {
}
