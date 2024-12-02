package com.example.gonggong_server.review.domain.repository;

import com.example.gonggong_server.review.domain.entity.ReportReview;

public interface ReportReviewRepository {
    ReportReview save(ReportReview reportReview);
    void deleteByReviewId(Long reviewId);
}
