package com.example.gonggong_server.chatroom.application.response;

import com.example.gonggong_server.chat.application.response.ChatMessageDTO;

import java.util.List;

public record ChatRoomCreateResponseDTO(Long chatRoomId, List<ChatMessageDTO> messages) {
}
