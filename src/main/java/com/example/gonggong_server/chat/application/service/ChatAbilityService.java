package com.example.gonggong_server.chat.application.service;

import com.example.gonggong_server.chat.api.request.ChatAbilityRequestDTO;
import com.example.gonggong_server.chat.application.response.ChatResponseDTO;
import com.example.gonggong_server.chat.domain.entity.Chat;
import com.example.gonggong_server.chat.domain.repository.ChatRepository;
import com.example.gonggong_server.chat.exception.ChatException;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.option.domain.entity.Option;
import com.example.gonggong_server.option.domain.repository.OptionRepository;
import com.example.gonggong_server.option.domain.value.Ability;
import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.program.domain.repository.ProgramRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatAbilityService {

    private final ChatRepository chatRepository;
    private final OptionRepository optionRepository;
    private final ProgramRepository programRepository;

    public ChatResponseDTO processAbilitiesAndRegion(ChatAbilityRequestDTO requestDTO, int pageSize, int pageIndex) {
        Long chatRoomId = requestDTO.chatRoomId();
        List<ChatResponseDTO.RecommendProgramDTO> programs = new ArrayList<>();

        if (requestDTO.abilities() != null && !requestDTO.abilities().isEmpty()) {
            saveAbilityChats(chatRoomId, requestDTO.abilities());

            String responseMessage = "마지막으로, 보다 알맞은 추천을 위해\n 아이가 사는 지역을 알려주세요.";
            saveResponseMessageChat(chatRoomId, true, responseMessage);

            return new ChatResponseDTO(
                    false,
                    responseMessage,
                    programs,
                    0,
                    0
            );
        }

        if (requestDTO.region() != null && !requestDTO.region().isBlank()) {
            Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
            Page<ChatResponseDTO.RecommendProgramDTO> programPage = findPrograms(chatRoomId, requestDTO.region(), pageable);

            saveProgramsAsChats(chatRoomId, programPage.getContent());

            return new ChatResponseDTO(
                    true,
                    "추천 프로그램을 확인하세요!",
                    programPage.getContent(),
                    programPage.getTotalPages(),
                    programPage.getNumber()+1
            );
        }

        // 요청 Body가 비어 있는 경우
        throw new ChatException(ErrorStatus.BAD_REQUEST);
    }


    private void saveAbilityChats(Long chatRoomId, List<Ability> abilities) {
        saveResponseMessageChat(chatRoomId, true, "키우고 싶은 아이의 능력치를 선택해주세요.\n 여러 개 선택할 수도 있어요");
        abilities.forEach(ability -> {
            Option option = Option.builder()
                    .chatRoomId(chatRoomId)
                    .ability(ability.getValue())
                    .build();
            optionRepository.save(option);
        });
        String abilityMessage = String.join(", ", abilities.stream()
                .map(Ability::getValue)
                .toList());
        saveResponseMessageChat(chatRoomId, false, abilityMessage);
    }
    private Page<ChatResponseDTO.RecommendProgramDTO> findPrograms(Long chatRoomId, String region, Pageable pageable) {
        // Option 기준 가져오기
        List<String> abilityValues = optionRepository.findByChatRoomId(chatRoomId).stream()
                .map(Option::getAbility)
                .toList();

        // 지역 기준과 능력치 기준으로 프로그램 검색 (페이징 적용)
        Page<Program> programs = programRepository.findByAbilitiesAndAddress(abilityValues, region, pageable);

        return programs.map(program -> ChatResponseDTO.RecommendProgramDTO.of(program));
    }

    private void saveProgramsAsChats(Long chatRoomId, List<ChatResponseDTO.RecommendProgramDTO> programs) {
        programs.forEach(program -> {
            try {
                // 프로그램 정보를 JSON 형식으로 변환
                String programJson = new ObjectMapper().writeValueAsString(program);

                Chat programChat = Chat.of(chatRoomId, true, programJson, null);
                chatRepository.save(programChat);

            } catch (JsonProcessingException e) {
                throw new ChatException(ErrorStatus.JSON_PARSE_ERROR);
            }
        });
    }
    private void saveResponseMessageChat(Long chatRoomId, Boolean isAuthor, String responseMessage){
        Chat chat = Chat.of(chatRoomId, isAuthor, responseMessage, null);
        chatRepository.save(chat);
    }
}
