package com.example.gonggong_server.chat.application.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record RecommendProgramDTO(
        Long programId,
        String programName,
        String facultyName,
        String programTarget,
        String programStartDate,
        String programEndDate
) {
    public RecommendProgramDTO(Long programId, String programName, String facultyName, String programTarget, LocalDate programStartDate, LocalDate programEndDate) {
        this(
                programId,
                programName,
                facultyName,
                programTarget,
                programStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                programEndDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
    }
}
