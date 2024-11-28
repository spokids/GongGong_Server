package com.example.gonggong_server.review.intra.repository;

import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.review.domain.entity.Review;
import com.example.gonggong_server.review.domain.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaReviewRepository extends ReviewRepository, JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.programId = :programId AND (:lastReviewId = 0 OR r.reviewId < :lastReviewId) ORDER BY r.createDate DESC")
    List<Review> findReviews(@Param("programId") Long programId,
                             @Param("lastReviewId") Long lastReviewId,
                             Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.userId = :userId AND (:lastReviewId = 0 OR r.reviewId < :lastReviewId) ORDER BY r.createDate DESC")
    List<Review> findReviewsByUserId(@Param("userId")Long userId,
                                     @Param("lastReviewId") Long lastReviewId,
                                     Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.programId = :programId ORDER BY r.createDate DESC")
    List<Review> findAllByProgramId(Long programId);

    @Query("SELECT DISTINCT p FROM Review r JOIN Program p ON r.programId = p.programId ORDER BY r.createDate DESC")
    Page<Program> findReviewedPrograms(Pageable pageable);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.userId = :userId")
    int countByUserId(Long userId);
}
