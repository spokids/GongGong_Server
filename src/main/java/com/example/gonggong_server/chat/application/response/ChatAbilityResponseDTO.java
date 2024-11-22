package com.example.gonggong_server.chat.application.response;

import java.util.List;

public record ChatAbilityResponseDTO(
        Boolean isSuccess,
        String responseMessage,
        List<RecommendProgramDTO> programs,
        int totalPage,
        int currentPage
) {
}
