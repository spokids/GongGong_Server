package com.example.gonggong_server.review.intra.repository;

import com.example.gonggong_server.review.domain.entity.ReportReview;
import com.example.gonggong_server.review.domain.repository.ReportReviewRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaReportReviewRepository extends ReportReviewRepository, JpaRepository<ReportReview, Long> {
    @Modifying
    @Query("DELETE FROM ReportReview rr WHERE rr.reviewId = :reviewId")
    void deleteByReviewId(@Param("reviewId") Long reviewId);
}
