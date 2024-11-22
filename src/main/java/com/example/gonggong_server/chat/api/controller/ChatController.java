package com.example.gonggong_server.chat.api.controller;

import com.example.gonggong_server.chat.api.request.ChatAbilityRequestDTO;
import com.example.gonggong_server.chat.api.request.ChatFreeRequestDTO;
import com.example.gonggong_server.chat.application.response.ChatAbilityResponseDTO;
import com.example.gonggong_server.chat.application.response.ChatFreeResponseDTO;
import com.example.gonggong_server.chat.application.response.ChatListResponseDTO;
import com.example.gonggong_server.chat.application.service.ChatAbilityService;
import com.example.gonggong_server.chat.application.service.ChatFreeService;
import com.example.gonggong_server.chat.application.service.ChatService;
import com.example.gonggong_server.global.response.ApiResponse;
import com.example.gonggong_server.global.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ChatController {

    private final ChatService chatService;
    private final ChatFreeService chatFreeService;
    private final ChatAbilityService chatAbilityService;

    @PostMapping("/v1/chat/completions")
    public ResponseEntity<ApiResponse<ChatFreeResponseDTO>> processFreeChat(
            @AuthenticationPrincipal String userInputId,
            @RequestBody ChatFreeRequestDTO request,
            @RequestParam(value = "size", defaultValue = "2") int size,
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        ChatFreeResponseDTO response = chatFreeService.handleUserInput(request, size, page);
        return ApiResponse.success(SuccessStatus.OK, response);
    }

    @DeleteMapping("/chat/{chatRoomId}")
    public ResponseEntity<ApiResponse<String>> deleteChat(
            @AuthenticationPrincipal String userInputId,
            @PathVariable Long chatRoomId
    ) {
        chatService.deleteChats(chatRoomId);
        return ApiResponse.success(SuccessStatus.OK);
    }

    @GetMapping("/chat")
    public ResponseEntity<ApiResponse<ChatListResponseDTO>> getChatList(
            @AuthenticationPrincipal String userInputId,
            @RequestParam(value = "size", defaultValue = "2") int size,
            @RequestParam(value = "page", defaultValue = "1") int page

    ) {
        ChatListResponseDTO response = chatService.getChats(userInputId, size, page);
        return ApiResponse.success(SuccessStatus.OK, response);
    }

    @PostMapping("/chat/ability")
    public ResponseEntity<ApiResponse<ChatAbilityResponseDTO>> processAbilityChat(
            @AuthenticationPrincipal String userInputId,
            @RequestBody ChatAbilityRequestDTO request,
            @RequestParam(value = "size", defaultValue = "2") int size,
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        ChatAbilityResponseDTO response = chatAbilityService.processAbilitiesAndRegion(request, size, page);
        return ApiResponse.success(SuccessStatus.OK, response);
    }
}
