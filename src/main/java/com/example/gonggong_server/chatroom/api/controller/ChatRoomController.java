package com.example.gonggong_server.chatroom.api.controller;

import com.example.gonggong_server.chatroom.api.request.ChatChoiceRequestDTO;
import com.example.gonggong_server.chatroom.application.response.ChatRoomCreateResponseDTO;
import com.example.gonggong_server.chatroom.application.service.ChatRoomService;
import com.example.gonggong_server.global.response.ApiResponse;
import com.example.gonggong_server.global.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatRoom")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    @PostMapping("/choice")
    public ResponseEntity<ApiResponse<ChatRoomCreateResponseDTO>> processChat(
            @AuthenticationPrincipal String userInputId,
            @RequestBody ChatChoiceRequestDTO request
            ) {
        ChatRoomCreateResponseDTO response = chatRoomService.createChatRoom(userInputId, request);
        return ApiResponse.success(SuccessStatus.OK, response);
    }
}
