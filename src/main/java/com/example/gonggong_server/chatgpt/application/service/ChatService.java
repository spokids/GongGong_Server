package com.example.gonggong_server.chatgpt.application.service;

import com.example.gonggong_server.chatgpt.api.request.ChatFreeRequestDTO;
import com.example.gonggong_server.chatgpt.application.response.ChatFreeResponseDTO;
import com.example.gonggong_server.chatgpt.application.response.RecommendProgramDTO;
import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.program.domain.repository.ProgramRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatgptService chatgptService;
    private final ProgramRepository programRepository;

    public ChatFreeResponseDTO handleUserInput(ChatFreeRequestDTO chatFreeRequestDTO) {
        String userFreeChat = chatFreeRequestDTO.userFreeInput();
        Map<String, String> currentCriteria = chatFreeRequestDTO.currentCriteria();
        if (currentCriteria == null) {
            currentCriteria = new HashMap<>();
        }
        // 1. ChatGPT를 사용하여 새 데이터 추출
        Map<String, String> parsedData = extractCriteriaFromChatGPT(userFreeChat);

        // 2. 이전 데이터와 병합
        currentCriteria.putAll(parsedData);

        // 3. 빠진 값 확인
        List<String> missingFields = findMissingFields(currentCriteria);

        if (!missingFields.isEmpty()) {
            return ChatFreeResponseDTO.builder()
                    .isSuccess(false)
                    .missingFields(missingFields)
                    .currentCriteria(currentCriteria) // 누적된 기준값
                    .programs(null)
                    .build();
        }

        // 4. 기준이 충족되면 프로그램 데이터 반환
        List<Program> programList = findPrograms(currentCriteria);
        List<RecommendProgramDTO> programs = programList.stream()
                .map(program -> new RecommendProgramDTO(
                        program.getProgramId(),
                        program.getProgramName(),
                        program.getFacultyName(),
                        program.getProgramTarget(),
                        program.getProgramStartDate(),
                        program.getProgramEndDate()))
                .toList();

        return ChatFreeResponseDTO.builder()
                .isSuccess(true)
                .missingFields(null)
                .currentCriteria(currentCriteria)
                .programs(programs)
                .build();
    }

    private Map<String, String> extractCriteriaFromChatGPT(String userFreeChat) {
        // ChatGPT에게 요청할 프롬프트 생성
        String prompt = String.format(
                "사용자가 다음과 같은 입력을 주었습니다: \"%s\". " +
                        "이 입력에서 '지역', '나이', '종목'을 추출하여 JSON 형식으로 반환하세요. " +
                        "만약 해당 값이 없다면 해당 필드는 빈 문자열로 반환하세요." +
                        "다른 설명 없이 JSON 형식만 반환하세요.",
                userFreeChat
        );

        String gptResponse = chatgptService.sendMessage(prompt);

        // JSON 응답 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(gptResponse, new TypeReference<Map<String, String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("ChatGPT 응답 파싱 실패: " + gptResponse, e);
        }
    }
    private List<Program> findPrograms(Map<String, String> currentCriteria) {
        String ageString = currentCriteria.get("나이");
        String location = currentCriteria.get("지역");
        String type = currentCriteria.get("종목");

        int age = parseAge(ageString);

        return programRepository.findProgramsByCriteria(age, location, type);
    }

    private int parseAge(String ageString) {
        try {
            // "7세"와 같은 문자열에서 숫자만 추출
            return Integer.parseInt(ageString.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("올바르지 않은 나이 형식입니다: " + ageString);
        }
    }

    private List<String> findMissingFields(Map<String, String> criteria) {
        List<String> missingFields = new ArrayList<>();
        if (!criteria.containsKey("지역") || criteria.get("지역").isBlank()) missingFields.add("지역");
        if (!criteria.containsKey("나이") || criteria.get("나이").isBlank()) missingFields.add("나이");
        if (!criteria.containsKey("종목") || criteria.get("종목").isBlank()) missingFields.add("종목");
        return missingFields;
    }
}

