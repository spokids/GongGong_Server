package com.example.gonggong_server.chat.application.service;

import com.example.gonggong_server.auth.exception.AuthException;
import com.example.gonggong_server.chat.application.response.ChatListResponseDTO;
import com.example.gonggong_server.chat.application.response.ChatResponseDTO;
import com.example.gonggong_server.chat.domain.entity.Chat;
import com.example.gonggong_server.chat.domain.repository.ChatRepository;
import com.example.gonggong_server.chat.exception.ChatException;
import com.example.gonggong_server.chatroom.domain.entity.ChatRoom;
import com.example.gonggong_server.chatroom.domain.repository.ChatRoomRepository;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.option.domain.entity.Option;
import com.example.gonggong_server.option.domain.repository.OptionRepository;
import com.example.gonggong_server.user.domain.entity.User;
import com.example.gonggong_server.user.domain.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final OptionRepository optionRepository;

    public void deleteChats(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId)
                .orElseThrow(()-> new ChatException(ErrorStatus.CHATROOM_NOT_EXIST));
        chatRoomRepository.delete(chatRoom);
        List<Chat> chats = chatRepository.findByChatRoomId(chatRoomId);
        for (Chat chat : chats) {
            chatRepository.delete(chat);
        }
        List<Option> options = optionRepository.findByChatRoomId(chatRoomId);
        for(Option option:options){
            optionRepository.delete(option);
        }
    }

    public ChatListResponseDTO getChats(String userInputId, int pageSize, int pageIndex) {
        User user = userRepository.findByUserInputId(userInputId)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));
        Long userId = user.getUserId();

        ChatRoom recentChatRoom = chatRoomRepository.findTopByUserIdOrderByCreateDateDesc(userId)
                .orElseThrow(() -> new ChatException(ErrorStatus.CHATROOM_NOT_EXIST));

        // 전체 Chats 가져오기
        List<Chat> chats = chatRepository.findByChatRoomIdOrderByCreateDateAsc(recentChatRoom.getChatRoomId());

        // 프로그램 관련 Chats와 일반 Chats 분리
        List<Chat> programChats = new ArrayList<>();
        List<String> chatMessages = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (Chat chat : chats) {
            if (isProgramChat(chat.getContent(), objectMapper)) {
                programChats.add(chat);
            } else {
                chatMessages.add(chat.getContent());
            }
        }
        List<Option> options = optionRepository.findByChatRoomId(recentChatRoom.getChatRoomId());
        List<String> optionValues = options.stream()
                .map(Option::getAbility)
                .toList();


        // 프로그램 Chats를 페이징 처리
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        List<ChatResponseDTO.RecommendProgramDTO> programDTOs = programChats.stream()
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .map(chat -> parseRecommendProgramFromChat(chat.getContent(), objectMapper)) // 바로 RecommendProgramDTO로 변환
                .filter(Objects::nonNull)
                .toList();

        int totalProgramPages = (int) Math.ceil((double) programChats.size() / pageSize);

        // ChatListResponseDTO 생성
        return new ChatListResponseDTO(chatMessages, programDTOs, optionValues, totalProgramPages, pageIndex);
    }

    private boolean isProgramChat(String content, ObjectMapper objectMapper) {
        try {
            objectMapper.readValue(content, ChatResponseDTO.RecommendProgramDTO.class); // RecommendProgramDTO로 바로 검증
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    private ChatResponseDTO.RecommendProgramDTO parseRecommendProgramFromChat(String content, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(content, ChatResponseDTO.RecommendProgramDTO.class); // 바로 RecommendProgramDTO로 변환
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}


