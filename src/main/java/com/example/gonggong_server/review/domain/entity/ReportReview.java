package com.example.gonggong_server.review.domain.entity;

import com.example.gonggong_server.review.domain.value.Report;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReportReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportReviewId;

    @NotNull
    private Long userId;

    @NotNull
    private Long reviewId;

    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    @Builder
    public static ReportReview of(Long userId, Long reviewId, Report report) {
        return ReportReview.builder()
                .userId(userId)
                .reviewId(reviewId)
                .content(report.getValue())
                .createDate(LocalDateTime.now())
                .build();
    }
}
