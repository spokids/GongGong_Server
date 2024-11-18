package com.example.gonggong_server.chatgpt.api.request;


import java.util.Map;

public record ChatFreeRequestDTO(String userFreeInput, Map<String, String>currentCriteria) {
}
