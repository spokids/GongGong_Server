package com.example.gonggong_server.review.application.service;

import com.example.gonggong_server.global.exception.GlobalException;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.review.api.request.ReportRequestDTO;
import com.example.gonggong_server.review.domain.entity.ReportReview;
import com.example.gonggong_server.review.domain.repository.ReportReviewRepository;
import com.example.gonggong_server.review.domain.value.Report;
import com.example.gonggong_server.user.domain.entity.User;
import com.example.gonggong_server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportReviewService {
    private final UserRepository userRepository;
    private final ReportReviewRepository reportReviewRepository;
    @Transactional
    public void postReviewReport(String userInputId, Long reviewId, ReportRequestDTO requestDTO){
        User user = userRepository.findByUserInputId(userInputId)
                .orElseThrow(() -> new GlobalException(ErrorStatus.USER_NOT_FOUND));

        long userId = user.getUserId();
        List<Report> reports = requestDTO.reports();
        reports.forEach(report -> {
            ReportReview reportReview = ReportReview.of(userId, reviewId, report);
            reportReviewRepository.save(reportReview);
        });
    }
}
