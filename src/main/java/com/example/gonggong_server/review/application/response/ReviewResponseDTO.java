package com.example.gonggong_server.review.application.response;

public record ReviewResponseDTO(Long reviewId) {

    public static ReviewResponseDTO of(Long reviewId) {
        return new ReviewResponseDTO(reviewId);
    }
}
