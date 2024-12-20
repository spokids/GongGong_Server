package com.example.gonggong_server.program.application.response;

import com.example.gonggong_server.program.domain.entity.Program;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgramListResponseDTO {
    private List<ProgramDTO> programList;
    private List<ConditionDTO> conditionList;
    private int totalPage;
    private int currentPage;

    public static ProgramListResponseDTO of(
            List<ProgramDTO> programList,
            List<ConditionDTO> conditionList,
            int totalPage,
            int currentPage
    ) {
        return ProgramListResponseDTO.builder()
                .programList(programList)
                .conditionList(conditionList)
                .totalPage(totalPage)
                .currentPage(currentPage)
                .build();
    }

    public static ProgramListResponseDTO of(
            List<ProgramDTO> programList,
            int totalPage,
            int currentPage
    ) {
        return ProgramListResponseDTO.builder()
                .programList(programList)
                .totalPage(totalPage)
                .currentPage(currentPage)
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProgramDTO {
        private Long programId;
        private String programType;
        private String programName;
        private String facilityName;
        private String programAge;
        private String programDate;

        public static ProgramDTO of(Program program) {
            return ProgramDTO.builder()
                    .programId(program.getProgramId())
                    .programType(program.getType())
                    .programName(program.getProgramName())
                    .facilityName(program.getFacultyName())
                    .programAge(formatProgramAge(program.getStartAge(), program.getEndAge()))
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConditionDTO {
        private String location;
        private String age;
        private List<String> existingTypes;
        private List<String> notExistingTypes;

        public static ConditionDTO of(
                String location,
                String age,
                List<String> existingTypes,
                List<String> notExistingTypes
        ) {
            return ConditionDTO.builder()
                    .location(location)
                    .age(age != null && !age.isEmpty() ? age : null)
                    .existingTypes(existingTypes != null && !existingTypes.isEmpty() ? existingTypes : null)
                    .notExistingTypes(notExistingTypes != null && !notExistingTypes.isEmpty() ? notExistingTypes : null)
                    .build();
        }
    }
}
