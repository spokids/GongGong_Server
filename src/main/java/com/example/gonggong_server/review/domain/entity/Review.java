package com.example.gonggong_server.review.domain.entity;

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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @NotNull
    private Long userId;
    @NotNull
    private Long programId;

    @NotNull
    @Column(length = 300)
    private String content;
    private String imageUrl;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    @Builder
    public static Review from(Long userId, Long programId, String content, String imageUrl) {
        return Review.builder()
                .userId(userId)
                .programId(programId)
                .content(content)
                .imageUrl(imageUrl)
                .build();
    }
}
