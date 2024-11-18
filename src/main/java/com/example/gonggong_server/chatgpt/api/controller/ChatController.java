package com.example.gonggong_server.chatgpt.api.controller;

import com.example.gonggong_server.auth.application.response.TokenResponseDTO;
import com.example.gonggong_server.chatgpt.api.request.ChatFreeRequestDTO;
import com.example.gonggong_server.chatgpt.application.response.ChatFreeResponseDTO;
import com.example.gonggong_server.chatgpt.application.service.ChatService;
import com.example.gonggong_server.global.response.ApiResponse;
import com.example.gonggong_server.global.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/chat/completions")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatFreeResponseDTO>> processChat(@RequestBody ChatFreeRequestDTO request) {
        ChatFreeResponseDTO response = chatService.handleUserInput(request);
        return ApiResponse.success(SuccessStatus.OK, response);
    }
}
