package com.example.gonggong_server.review.application.response;

import com.example.gonggong_server.review.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListResponseDTO {
    private List<ReviewDTO> reviews;
    private Boolean hasNext;

    public static ReviewListResponseDTO of(List<ReviewDTO> reviews, Boolean hasNext) {
        return ReviewListResponseDTO.builder()
                .reviews(reviews)
                .hasNext(hasNext)
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewDTO {
        private Long reviewId;
        private String nickName;
        private String content;
        private String imageUrl;
        private String createdAt; // 형식 변경된 날짜 문자열

        public static ReviewDTO of(Review review, Map<Long, String> userNicknames) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
            String nickName = userNicknames.getOrDefault(review.getUserId(), "알 수 없음");


            return ReviewDTO.builder()
                    .reviewId(review.getReviewId())
                    .nickName(nickName)
                    .content(review.getContent())
                    .imageUrl(review.getImageUrl())
                    .createdAt(review.getCreateDate().format(formatter))
                    .build();
        }
    }
}
