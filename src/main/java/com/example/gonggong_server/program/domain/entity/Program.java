package com.example.gonggong_server.program.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programId;

    @NotNull
    private String facultyName;
    @NotNull
    private String facilityType;
    @NotNull
    private String provinceName;
    @NotNull
    private String districtName;
    @NotNull
    private String facilityAddress;
    @NotNull
    private String programName;
    @NotNull
    private String programTarget;
    @NotNull
    private LocalDate programStartDate;
    @NotNull
    private LocalDate programEndDate;
    private String programWeekDay;
    @NotNull
    private String programTimeZone;
    @NotNull
    private Integer programRecruitNumber;
    private Integer programPrice;
    private String homepageUrl;
    private String participantCount;
    private Boolean leaderQualification;
    @NotNull
    private String type;
    @NotNull
    private String subDistrict;
    @NotNull
    private String fullAddress;
    private String ability;
    @NotNull
    private Integer startAge;
    @NotNull
    private Integer endAge;
    private Time startTime;
    private Time endTime;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;
}
