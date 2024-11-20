package com.example.gonggong_server.review.intra.repository;

import com.example.gonggong_server.review.domain.entity.Review;
import com.example.gonggong_server.review.domain.repository.ReviewRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReviewRepository extends ReviewRepository, JpaRepository<Review, Long> {
}
