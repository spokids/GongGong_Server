package com.example.gonggong_server.chat.application.response;

import com.example.gonggong_server.program.domain.entity.Program;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponseDTO {
    Boolean isSuccess;
    String responseMessage;
    List<RecommendProgramDTO> programs;
    int totalPage;
    int currentPage;

    public static ChatResponseDTO of(
            Boolean isSuccess,
            String responseMessage,
            List<RecommendProgramDTO> programs,
            int totalPage,
            int currentPage
    )
    {
        return ChatResponseDTO.builder()
                .isSuccess(isSuccess)
                .responseMessage(responseMessage)
                .programs(programs)
                .totalPage(totalPage)
                .currentPage(currentPage)
                .build();

    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendProgramDTO{
        Long programId;
        String programType;
        String programName;
        String facultyName;
        String programTarget;
        String programDate;
        public static RecommendProgramDTO of(
                Program program
        ){
            return RecommendProgramDTO.builder()
                    .programId(program.getProgramId())
                    .programType(program.getType())
                    .programName(program.getProgramName())
                    .facultyName(program.getFacultyName())
                    .programTarget(formatProgramAge(program.getStartAge(), program.getEndAge()))
                    .programDate(formatProgramDate(program.getProgramStartDate(), program.getProgramEndDate()))
                    .build();

        }
        // 날짜를 "yy.MM.dd-yy.MM.dd" 형식으로 변환
        private static String formatProgramDate(LocalDate startDate, LocalDate endDate) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
            return startDate.format(formatter) + " - " + endDate.format(formatter);
        }

        // 나이를 "만 x세~만 y세" 형식으로 변환
        private static String formatProgramAge(int startAge, int endAge) {
            return "만 " + startAge + "세 ~ 만 " + endAge + "세";
        }
    }
}
