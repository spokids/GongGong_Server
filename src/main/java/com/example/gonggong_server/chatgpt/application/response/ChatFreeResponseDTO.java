package com.example.gonggong_server.chatgpt.application.response;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ChatFreeResponseDTO(
        Boolean isSuccess,
        List<String> missingFields,
        Map<String, String> currentCriteria,
        List<RecommendProgramDTO> programs) {
}
