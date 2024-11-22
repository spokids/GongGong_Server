package com.example.gonggong_server.chat.application.response;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ChatFreeResponseDTO(
        Boolean isSuccess,
        String responseMessage,
        List<RecommendProgramDTO> programs,
        int totalPage,
        int currentPage) {
}
