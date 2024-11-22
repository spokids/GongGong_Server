package com.example.gonggong_server.review.domain.repository;

import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.review.domain.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    void deleteByUserId(Long userId);
    void delete(Review review);
    Review save(Review review);
    List<Review> findAllByProgramId(Long programId);
    Page<Program> findReviewedPrograms(Pageable pageable);
    Page<Review> findReviews(Long userId, Pageable pageable);
    Optional<Review> findByReviewId(Long reviewId);
    Optional<Review> findByUserId(Long userId);
}
