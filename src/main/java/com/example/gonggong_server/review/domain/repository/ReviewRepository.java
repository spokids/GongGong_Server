package com.example.gonggong_server.review.domain.repository;

import com.example.gonggong_server.review.domain.entity.Review;

import java.util.List;

public interface ReviewRepository {
    void deleteByUserId(Long userId);
    Review save(Review review);
    List<Review> findAllByProgramId(Long programId);
}
