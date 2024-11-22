package com.example.gonggong_server.chat.api.controller;

import com.example.gonggong_server.chat.api.request.ChatFreeRequestDTO;
import com.example.gonggong_server.chat.application.response.ChatFreeResponseDTO;
import com.example.gonggong_server.chat.application.service.ChatService;
import com.example.gonggong_server.global.response.ApiResponse;
import com.example.gonggong_server.global.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/chat/completions")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatFreeResponseDTO>> processChat(
            @AuthenticationPrincipal String userInputId,
            @RequestBody ChatFreeRequestDTO request,
            @RequestParam(value = "size", defaultValue = "2") int size,
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        ChatFreeResponseDTO response = chatService.handleUserInput(userInputId,request, size, page);
        return ApiResponse.success(SuccessStatus.OK, response);
    }
}
