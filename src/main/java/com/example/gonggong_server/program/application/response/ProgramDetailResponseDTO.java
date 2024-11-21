package com.example.gonggong_server.program.application.response;

import com.example.gonggong_server.program.domain.entity.Program;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDetailResponseDTO {
    private Long programId;
    private String programName;
    private String programType;
    private Boolean leaderQualification;


    private String facultyName;
    private String address;
    private String programAge;
    private Integer programRecruitNumber;

    private String programDate;
    private List<String> programWeekDay;
    private String programTime;

    private String homepageUrl;
    private Boolean isScripted;

    private int reviewCount;

    public static ProgramDetailResponseDTO of(Program program, int reviewsCount, Boolean isScrapped) {
        return ProgramDetailResponseDTO.builder()
                .programId(program.getProgramId())
                .programName(program.getProgramName())
                .programType(program.getType())
                .leaderQualification(program.getLeaderQualification())
                .facultyName(program.getFacultyName())
                .address(program.getFullAddress())
                .programAge(formatProgramAge(program.getStartAge(), program.getEndAge()))
                .programRecruitNumber(program.getProgramRecruitNumber())
                .programDate(formatProgramDate(program.getProgramStartDate(), program.getProgramEndDate()))
                .programWeekDay(List.of(program.getProgramWeekDay().split("")))
                .programTime(formatProgramTime(program.getStartTime(), program.getEndTime()))
                .homepageUrl(program.getHomepageUrl())
                .isScripted(isScrapped)
                .reviewCount(reviewsCount)
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

    // 시간을 "HH:mm ~ HH:mm" 형식으로 변환
    private static String formatProgramTime(Time startTime, Time endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return startTime.toLocalTime().format(formatter) + " ~ " + endTime.toLocalTime().format(formatter);
    }
}
