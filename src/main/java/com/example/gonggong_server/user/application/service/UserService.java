package com.example.gonggong_server.user.application.service;

import com.example.gonggong_server.auth.exception.AuthException;
import com.example.gonggong_server.chat.application.service.ChatService;
import com.example.gonggong_server.chat.domain.repository.ChatRepository;
import com.example.gonggong_server.chatroom.domain.repository.ChatRoomRepository;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.review.domain.repository.ReviewRepository;
import com.example.gonggong_server.scrap.domain.repository.ScrapRepository;
import com.example.gonggong_server.user.domain.entity.User;
import com.example.gonggong_server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ReviewRepository reviewRepository;
    private final ChatService chatService;

    @Transactional
    public void deleteUser(String userInputId) {
        System.out.println(userInputId);
        User user = findUserById(userInputId);
        Long userId = user.getUserId();

        deleteUserRelatedData(userId);

        userRepository.delete(user);
    }
    private User findUserById(String userInputId) {
        return userRepository.findByUserInputId(userInputId)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));
    }
    private void deleteUserRelatedData(Long userId) {
        scrapRepository.deleteByUserId(userId);
        reviewRepository.deleteByUserId(userId);
        List<Long> chatroomIds = chatRoomRepository.findAllIdsByUserId(userId);
        for(Long chatRoomId : chatroomIds){
            chatService.deleteChats(chatRoomId);
        }
    }
}
