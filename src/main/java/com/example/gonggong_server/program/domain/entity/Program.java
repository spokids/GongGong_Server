package com.example.gonggong_server.program.domain.entity;

import com.example.gonggong_server.program.domain.value.Ability;
import com.example.gonggong_server.program.domain.value.ProvinceName;
import com.example.gonggong_server.program.domain.value.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    @Enumerated(EnumType.STRING)
    private ProvinceName provinceName;
    @NotNull
    private String districtName;
    @NotNull
    private String facilityAdrress;
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
    @Enumerated(EnumType.STRING)
    private Type type;
    @NotNull
    private String subDistrict;
    @NotNull
    private String fullAddress;
    @Enumerated(EnumType.STRING)
    private Ability ability;
    @NotNull
    private Integer startAge;
    @NotNull
    private Integer endAge;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;
}
