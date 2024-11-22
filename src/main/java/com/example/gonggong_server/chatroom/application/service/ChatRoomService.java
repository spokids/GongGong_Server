package com.example.gonggong_server.chatroom.application.service;

import com.example.gonggong_server.auth.exception.AuthException;
import com.example.gonggong_server.chat.exception.ChatException;
import com.example.gonggong_server.chatroom.api.request.ChatChoiceRequestDTO;
import com.example.gonggong_server.chatroom.application.response.ChatRoomCreateResponseDTO;
import com.example.gonggong_server.chat.application.response.ChatMessageDTO;
import com.example.gonggong_server.chat.domain.entity.Chat;
import com.example.gonggong_server.chat.domain.repository.ChatRepository;
import com.example.gonggong_server.chat.domain.value.Choice;
import com.example.gonggong_server.chatroom.domain.entity.ChatRoom;
import com.example.gonggong_server.chatroom.domain.repository.ChatRoomRepository;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.user.domain.entity.User;
import com.example.gonggong_server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    public ChatRoomCreateResponseDTO createChatRoom(String userInputId, ChatChoiceRequestDTO requestDTO) {
        Choice choice = requestDTO.choice();
        User user = userRepository.findByUserInputId(userInputId)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));

        // ChatRoom 생성 및 저장
        ChatRoom newChatRoom = chatRoomRepository.save(
                ChatRoom.builder()
                        .userId(user.getUserId())
                        .createDate(LocalDateTime.now())
                        .build()
        );

        //기본 chat 저장
        List<Chat> initialChats = createInitialChats(newChatRoom.getChatRoomId(), choice);
        for (Chat chat : initialChats) {
            chatRepository.save(chat);
        }

        List<ChatMessageDTO> messages = createDefaultMessages(initialChats, choice);

        return new ChatRoomCreateResponseDTO(newChatRoom.getChatRoomId(), messages);
    }

    private List<Chat> createInitialChats(Long chatRoomId, Choice choice) {
        if (choice == Choice.FREE_CHAT) {
            return List.of(
                    Chat.builder()
                            .chatRoomId(chatRoomId)
                            .author(false)
                            .content("자유롭게 아이에게 맞는 프로그램을 찾고 싶어요")
                            .createDate(LocalDateTime.now())
                            .build(),
                    Chat.builder()
                            .chatRoomId(chatRoomId)
                            .author(true)
                            .content("자유롭게 아이의 나이, 거주하는 행정구역,\n또는 원하는 운동 종목 등을 입력해주세요!\n아이에게 맞는 체육 프로그램을 열심히 찾아드릴게요!")
                            .createDate(LocalDateTime.now())
                            .build()
            );
        } else if (choice == Choice.ABILITY_CHAT) {
            return List.of(
                    Chat.builder()
                            .chatRoomId(chatRoomId)
                            .author(false)
                            .content("키우고 싶은 능력치를 기준으로 찾고 싶어요")
                            .createDate(LocalDateTime.now())
                            .build(),
                    Chat.builder()
                            .chatRoomId(chatRoomId)
                            .author(true)
                            .content("키우고 싶은 아이의 능력치를 선택해주세요.\n여러 개 선택할 수도 있어요.")
                            .createDate(LocalDateTime.now())
                            .build()
            );
        }
        throw new ChatException(ErrorStatus.INVALID_CHOICE);
    }

    private List<ChatMessageDTO> createDefaultMessages(List<Chat> initialChats, Choice choice) {
        if (choice == Choice.FREE_CHAT) {
            return List.of(
                    new ChatMessageDTO(initialChats.get(0).getContent(), null),
                    new ChatMessageDTO(initialChats.get(1).getContent(), null)
            );
        } else if (choice == Choice.ABILITY_CHAT) {
            List<String> options = List.of(
                    "지구력", "민첩성", "협동력", "근력", "유연성",
                    "반응속도", "표현력", "균형감각", "집중력", "순발력", "정밀성"
            );
            return List.of(
                    new ChatMessageDTO(initialChats.get(0).getContent(), null),
                    new ChatMessageDTO(initialChats.get(1).getContent(), options)
            );
        }
        throw new ChatException(ErrorStatus.INVALID_CHOICE);
    }
}
