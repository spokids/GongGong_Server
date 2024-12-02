package com.example.gonggong_server.review.domain.repository;

import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.review.domain.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    void deleteByUserId(Long userId);
    void delete(Review review);
    Review save(Review review);
    List<Review> findReviews(Long programId, Long lastReviewId,Pageable pageable);
    List<Review> findAllByProgramId(Long programId);
    Page<Program> findReviewedPrograms(Pageable pageable);
    int countByUserId(Long userId);
    Optional<Review> findByReviewId(Long reviewId);
    List<Review> findReviewsByUserId(Long userId, Long lastReviewId, Pageable pageable);
    List<Review> findReviewsExcludingReported(Long programId, Long lastReviewId, Long userId, Pageable pageable);
}
