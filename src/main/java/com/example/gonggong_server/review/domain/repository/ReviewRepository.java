package com.example.gonggong_server.review.domain.repository;

import com.example.gonggong_server.review.domain.entity.Review;

public interface ReviewRepository {
    void deleteByUserId(Long userId);
    Review save(Review review);
}
