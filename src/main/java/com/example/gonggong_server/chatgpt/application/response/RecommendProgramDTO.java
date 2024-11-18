package com.example.gonggong_server.chatgpt.application.response;

import java.time.LocalDate;

public record RecommendProgramDTO(
        Long programId,
        String programName,
        String facilityName,
        String programTarget,
        LocalDate programStartDate,
        LocalDate programEndDate) {
}
