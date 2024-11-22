package com.example.gonggong_server.chatroom.application.response;

import java.util.List;

public record ChatRoomCreateResponseDTO(Long chatRoomId, List<DefaultMessageDTO> messages) {
}
