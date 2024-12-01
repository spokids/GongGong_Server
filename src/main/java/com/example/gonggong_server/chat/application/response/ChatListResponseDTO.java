package com.example.gonggong_server.chat.application.response;

import java.util.List;

public record ChatListResponseDTO(
        List<ChatDTO> chats,
        List<String> options,
        List<ChatResponseDTO.RecommendProgramDTO> programs,
        int totalProgramPages,
        int currentProgramPage
) {
}
