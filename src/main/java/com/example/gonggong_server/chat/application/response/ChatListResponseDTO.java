package com.example.gonggong_server.chat.application.response;

import java.util.List;

public record ChatListResponseDTO(
        List<String> chats,
        List<ChatResponseDTO.RecommendProgramDTO> programs,
        List<String> options,
        int totalProgramPages,
        int currentProgramPage
) {
}
