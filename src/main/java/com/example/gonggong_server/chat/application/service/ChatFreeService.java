package com.example.gonggong_server.chat.application.service;

import com.example.gonggong_server.chat.api.request.ChatFreeRequestDTO;
import com.example.gonggong_server.chat.application.response.ChatResponseDTO;
import com.example.gonggong_server.chat.domain.entity.Chat;
import com.example.gonggong_server.chat.domain.repository.ChatRepository;
import com.example.gonggong_server.chat.exception.ChatException;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.program.domain.repository.ProgramRepository;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ChatFreeService {
    private static final Set<String> VALID_TYPES = Set.of(
            "수영", "배드민턴", "농구", "기타", "헬스", "에어로빅", "탁구",
            "댄스", "요가", "발레", "필라테스", "빙상", "골프", "스피닝",
            "스쿼시", "배구", "축구", "검도", "무용", "자세교정", "피아노",
            "인라인", "유도", "볼링", "음악줄넘기", "테니스"
    );

    private final ChatgptService chatgptService;
    private final ChatRepository chatRepository;
    private final ProgramRepository programRepository;
    public ChatResponseDTO handleUserInput(ChatFreeRequestDTO chatFreeRequestDTO, int pageSize, int pageIndex) {

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

        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Program> programsPage = fetchRecommendedPrograms(currentCriteria, pageable);

        // 성공 응답 반환
        return buildSuccessResponse(chatRoomId, currentCriteria, programsPage);
    }


    private Map<String, String> initializeCurrentCriteria(Long chatRoomId) {
        // 최근 Chat 엔티티에서 currentCriteria 가져오기
        Chat latestChat = chatRepository.findFirstByChatRoomIdOrderByCreateDateDesc(chatRoomId);
        if (latestChat == null || latestChat.getCurrentCriteria() == null) {
            return new HashMap<>();
        }

        try {
            return new ObjectMapper().readValue(latestChat.getCurrentCriteria(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new ChatException(ErrorStatus.JSON_PARSE_ERROR);
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
            return parseCriteriaWithRegex(gptResponse);
        }
    }
    private Map<String, String> parseCriteriaWithRegex(String response) {
        Map<String, String> criteria = new HashMap<>();

        Matcher regionMatcher = Pattern.compile("\"지역\"\\s*:\\s*\"([^\"]*)\"").matcher(response);
        if (regionMatcher.find()) {
            criteria.put("지역", regionMatcher.group(1));
        } else {
            criteria.put("지역", "");
        }

        Matcher ageMatcher = Pattern.compile("\"나이\"\\s*:\\s*\"([^\"]*)\"").matcher(response);
        if (ageMatcher.find()) {
            criteria.put("나이", ageMatcher.group(1));
        } else {
            criteria.put("나이", "");
        }

        Matcher typeMatcher = Pattern.compile("\"종목\"\\s*:\\s*\"([^\"]*)\"").matcher(response);
        if (typeMatcher.find()) {
            criteria.put("종목", typeMatcher.group(1));
        } else {
            criteria.put("종목", "");
        }

        return criteria;
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
            throw new ChatException(ErrorStatus.NOT_RIGHT_AGE);
        }
    }

    private ChatResponseDTO buildFailureResponse(Long chatRoomId, Map<String, String> currentCriteria, List<String> missingFields) {

        String responseMessage = String.join("와(과) ", missingFields) + "을(를) 입력해주세요.";

        saveChat(chatRoomId, true, responseMessage, currentCriteria);

        return ChatResponseDTO.builder()
                .isSuccess(false)
                .responseMessage(responseMessage)
                .programs(null)
                .build();
    }

    private ChatResponseDTO buildSuccessResponse(Long chatRoomId, Map<String, String> currentCriteria, Page<Program> programsPage) {
        String responseMessage = "딱 맞을 만한 프로그램들을 찾아봤어요!";

        int totalPageCount = programsPage.getTotalPages();
        int currentPage = programsPage.getNumber() + 1;

        // Chat 저장
        saveChat(chatRoomId, true, responseMessage, currentCriteria);

        // RecommendProgramDTO 리스트 생성
        List<ChatResponseDTO.RecommendProgramDTO> programs = programsPage.stream()
                .map(program -> ChatResponseDTO.RecommendProgramDTO.of(program))
                .toList();
        // Chat 저장 (각 프로그램 개별 정보)
        programsPage.forEach(program -> {
            try {
                String programJson = new ObjectMapper().writeValueAsString(ChatResponseDTO.RecommendProgramDTO.of(program));

                saveChat(chatRoomId, true, programJson, currentCriteria);
            } catch (JsonProcessingException e) {
                throw new ChatException(ErrorStatus.JSON_PARSE_ERROR);
            }
        });

        return ChatResponseDTO.builder()
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
            throw new ChatException(ErrorStatus.JSON_PARSE_ERROR);
        }
    }
}
