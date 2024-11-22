package com.example.gonggong_server.chat.application.service;

import com.example.gonggong_server.auth.exception.AuthException;
import com.example.gonggong_server.chat.application.response.RecommendProgramDTO;
import com.example.gonggong_server.chat.api.request.ChatFreeRequestDTO;
import com.example.gonggong_server.chat.application.response.ChatFreeResponseDTO;
import com.example.gonggong_server.chat.domain.entity.Chat;
import com.example.gonggong_server.chat.domain.repository.ChatRepository;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.program.domain.repository.ProgramRepository;
import com.example.gonggong_server.user.domain.entity.User;
import com.example.gonggong_server.user.domain.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {
    private static final Set<String> VALID_TYPES = Set.of(
            "수영", "배드민턴", "농구", "기타", "헬스", "에어로빅", "탁구",
            "댄스", "요가", "발레", "필라테스", "빙상", "골프", "스피닝",
            "스쿼시", "배구", "축구", "검도", "무용", "자세교정", "피아노",
            "인라인", "유도", "볼링", "음악줄넘기", "테니스"
    );

    private final ChatgptService chatgptService;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ProgramRepository programRepository;

    public ChatFreeResponseDTO handleUserInput(String userInputId, ChatFreeRequestDTO chatFreeRequestDTO, int pageSize, int pageIndex) {
        User user = userRepository.findByUserInputId(userInputId)
                .orElseThrow(() -> new AuthException(ErrorStatus.USER_NOT_FOUND));
        Long userId = user.getUserId();
        Long chatRoomId = chatFreeRequestDTO.chatRoomId();

        // 이전 기준 가져오기
        Map<String, String> currentCriteria = initializeCurrentCriteria(chatRoomId);

        // 사용자 입력 저장
        saveChat(chatRoomId, false, chatFreeRequestDTO.userFreeInput(), currentCriteria);

        // 기준 추출 및 병합
        Map<String, String> parsedData = extractCriteriaFromChatGPT(chatFreeRequestDTO.userFreeInput());
        mergeCriteria(currentCriteria, parsedData);

        List<String> missingFields = findMissingFields(currentCriteria);

        if (!missingFields.isEmpty()) {
            return buildFailureResponse(chatRoomId, currentCriteria, missingFields);
        }

        // 페이징 처리
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Program> programsPage = fetchRecommendedPrograms(currentCriteria, pageable);

        // 성공 응답 반환
        return buildSuccessResponse(chatRoomId, currentCriteria, programsPage);
    }


    private Map<String, String> initializeCurrentCriteria(Long chatRoomId) {
        // 최근 Chat 엔티티에서 currentCriteria 가져오기
        Chat latestChat = chatRepository.findFirstByChatRoomIdOrderByCreateDateDesc(chatRoomId);
        if (latestChat == null || latestChat.getCurrentCriteria() == null) {
            // Chat이 없거나 currentCriteria가 null인 경우 빈 HashMap 반환
            return new HashMap<>();
        }

        try {
            return new ObjectMapper().readValue(latestChat.getCurrentCriteria(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("currentCriteria 파싱 실패", e);
        }
    }

    private Map<String, String> extractCriteriaFromChatGPT(String userFreeChat) {
        String prompt = String.format(
                "사용자가 다음과 같은 입력을 주었습니다: \"%s\". " +
                        "이 입력에서 '지역', '나이', '종목'을 추출하여 JSON 형식으로 반환하세요. " +
                        "만약 해당 값이 없다면 해당 필드는 빈 문자열로 반환하세요." +
                        "다른 설명 없이 JSON 형식만 반환하세요.",
                userFreeChat
        );

        String gptResponse = chatgptService.sendMessage(prompt);
        try {
            return new ObjectMapper().readValue(gptResponse, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("ChatGPT 응답 파싱 실패: " + gptResponse, e);
        }
    }

    private void mergeCriteria(Map<String, String> currentCriteria, Map<String, String> parsedData) {
        parsedData.forEach((key, value) -> {
            if (value != null && !value.isBlank()) {
                currentCriteria.put(key, value);
            }
        });
        validateAndCleanParsedData(currentCriteria);
    }

    private void validateAndCleanParsedData(Map<String, String> parsedData) {
        String type = parsedData.get("종목");
        if (type != null && !VALID_TYPES.contains(type)) {
            parsedData.remove("종목");
        }
    }

    private List<String> findMissingFields(Map<String, String> criteria) {
        List<String> missingFields = new ArrayList<>();
        if (!criteria.containsKey("지역") || criteria.get("지역").isBlank()) missingFields.add("지역");
        if (!criteria.containsKey("나이") || criteria.get("나이").isBlank()) missingFields.add("나이");
        String type = criteria.get("종목");
        if (type == null || type.isBlank() || !VALID_TYPES.contains(type)) missingFields.add("종목");
        return missingFields;
    }

    private Page<Program> fetchRecommendedPrograms(Map<String, String> criteria, Pageable pageable) {
        return programRepository.findProgramsByCriteria(
                parseAge(criteria.get("나이")),
                criteria.get("지역"),
                criteria.get("종목"),
                pageable
        );
    }


    private int parseAge(String ageString) {
        try {
            return Integer.parseInt(ageString.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("올바르지 않은 나이 형식입니다: " + ageString);
        }
    }

    private ChatFreeResponseDTO buildFailureResponse(Long chatRoomId, Map<String, String> currentCriteria, List<String> missingFields) {

        String responseMessage = String.join("와(과) ", missingFields) + "을(를) 입력해주세요.";

        saveChat(chatRoomId, true, responseMessage, currentCriteria);

        return ChatFreeResponseDTO.builder()
                .isSuccess(false)
                .responseMessage(responseMessage)
                .programs(null)
                .build();
    }

    private ChatFreeResponseDTO buildSuccessResponse(Long chatRoomId, Map<String, String> currentCriteria, Page<Program> programsPage) {
        String responseMessage = "추천 프로그램을 확인하세요!";

        int totalPageCount = programsPage.getTotalPages();
        int currentPage = programsPage.getNumber() + 1;

        // Chat 저장
        saveChat(chatRoomId, true, responseMessage, currentCriteria);

        // RecommendProgramDTO 리스트 생성
        List<RecommendProgramDTO> programs = programsPage.stream()
                .map(program -> new RecommendProgramDTO(
                        program.getProgramId(),
                        program.getProgramName(),
                        program.getFacultyName(),
                        program.getProgramTarget(),
                        program.getProgramStartDate(),
                        program.getProgramEndDate()
                ))
                .toList();

        return ChatFreeResponseDTO.builder()
                .isSuccess(true)
                .responseMessage(responseMessage)
                .programs(programs)
                .totalPage(totalPageCount)
                .currentPage(currentPage)
                .build();
    }

    private void saveChat(Long chatRoomId, Boolean author, String content, Map<String, String> currentCriteria) {
        try {
            // currentCriteria를 JSON 형식으로 변환
            String criteriaJson = new ObjectMapper().writeValueAsString(currentCriteria);

            Chat chat = Chat.builder()
                    .chatRoomId(chatRoomId)
                    .author(author)
                    .content(content)
                    .currentCriteria(criteriaJson)
                    .createDate(LocalDateTime.now())
                    .build();

            chatRepository.save(chat);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("currentCriteria 저장 실패", e);
        }
    }
}


