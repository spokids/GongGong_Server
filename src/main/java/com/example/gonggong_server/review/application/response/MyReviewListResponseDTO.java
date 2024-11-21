package com.example.gonggong_server.review.application.response;

import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.review.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyReviewListResponseDTO {
    private String nickName;
    private String userInputId;
    private List<MyReviewDTO> reviews;
    private int totalPage;
    private int currentPage;

    public static MyReviewListResponseDTO of(String nickName, String userInputId, List<MyReviewDTO> reviews, int totalPage, int currentPage){
        return MyReviewListResponseDTO.builder()
                .nickName(nickName)
                .reviews(reviews)
                .userInputId(userInputId)
                .totalPage(totalPage)
                .currentPage(currentPage)
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyReviewDTO {
        private Long reviewId;
        private String programName;
        private String content;
        private String imageUrl;
        private String createdAt;

        public static MyReviewDTO of(Review review, Program program){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
            return MyReviewDTO.builder()
                    .reviewId(review.getReviewId())
                    .programName(program.getProgramName())
                    .content(review.getContent())
                    .imageUrl(review.getImageUrl())
                    .createdAt(review.getCreateDate().format(formatter))
                    .build();
        }
    }
}
