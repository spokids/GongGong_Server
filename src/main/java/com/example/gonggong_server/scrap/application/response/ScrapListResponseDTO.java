package com.example.gonggong_server.scrap.application.response;

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
public class ScrapListResponseDTO{
    private String nickName;
    private String userInputId;
    private List<ScrapProgramDTO> scraps;
    private Boolean hasNext;

    public static ScrapListResponseDTO of(
            String nickName,
            String userInputId,
            List<ScrapProgramDTO> scraps,
            Boolean hasNext
    ) {
        return ScrapListResponseDTO.builder()
                .nickName(nickName)
                .userInputId(userInputId)
                .scraps(scraps)
                .hasNext(hasNext)
                .build();
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScrapProgramDTO {
        private Long scrapId;
        private Long programId;
        private String programType;
        private String programName;
        private String facilityName;
        private String programAge;
        private String programDate;

        public static ScrapListResponseDTO.ScrapProgramDTO of(Long scrapId, Long programId, String programType,String programName, String facilityName,
                                                           int startAge, int endAge, LocalDate startDate, LocalDate endDate) {
            return ScrapProgramDTO.builder()
                    .scrapId(scrapId)
                    .programId(programId)
                    .programType(programType)
                    .programName(programName)
                    .facilityName(facilityName)
                    .programAge(formatProgramAge(startAge, endAge))
                    .programDate(formatProgramDate(startDate, endDate))
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
