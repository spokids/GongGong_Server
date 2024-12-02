package com.example.gonggong_server.review.api.controller;

import com.example.gonggong_server.global.response.ApiResponse;
import com.example.gonggong_server.global.status.SuccessStatus;
import com.example.gonggong_server.review.api.request.ReportRequestDTO;
import com.example.gonggong_server.review.api.request.ReviewRequestDTO;
import com.example.gonggong_server.review.application.response.MyReviewListResponseDTO;
import com.example.gonggong_server.review.application.response.ReviewListResponseDTO;
import com.example.gonggong_server.review.application.response.ReviewResponseDTO;
import com.example.gonggong_server.review.application.service.ReportReviewService;
import com.example.gonggong_server.review.application.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReportReviewService reportReviewService;

    @PostMapping("/create/{programId}")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> createReview(
            @AuthenticationPrincipal String userInputId,
            @PathVariable Long programId,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "request") @Valid ReviewRequestDTO request
    ) throws IOException {
        ReviewResponseDTO response = reviewService.createReview(userInputId, programId, image, request);
        return ApiResponse.success(SuccessStatus.CREATED, response);

    }

    @GetMapping("/list/{programId}")
    public ResponseEntity<ApiResponse<ReviewListResponseDTO>> getReviews(
            @PathVariable Long programId,
            @RequestParam(name = "lastReviewId", defaultValue = "0") Long lastReviewId,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestHeader(name = "Authorization", required = false) String token
    ) {
        ReviewListResponseDTO response = reviewService.getReviews(programId, lastReviewId, size, token);
        return ApiResponse.success(SuccessStatus.OK, response);
    }

    @GetMapping("/mypage")
    public ResponseEntity<ApiResponse<MyReviewListResponseDTO>> getMyReviews(
            @AuthenticationPrincipal String userInputId,
            @RequestParam(name = "lastReviewId", defaultValue = "0") Long lastReviewId,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        MyReviewListResponseDTO response = reviewService.getMyReviews(userInputId, size, lastReviewId);
        return ApiResponse.success(SuccessStatus.OK, response);
    }

    @DeleteMapping("/mypage/{reviewId}")
    public ResponseEntity<ApiResponse<String>> deleteMyReview(
            @AuthenticationPrincipal String userInputId,
            @PathVariable Long reviewId
    ){
        reviewService.deleteMyReview(userInputId,reviewId);
        return ApiResponse.success(SuccessStatus.OK);
    }

    @PostMapping("/report/{reviewId}")
    public ResponseEntity<ApiResponse<String>> postReviewReport(
            @AuthenticationPrincipal String userInputId,
            @PathVariable Long reviewId,
            @RequestBody ReportRequestDTO request
    ){
        reportReviewService.postReviewReport(userInputId, reviewId, request);
        return ApiResponse.success(SuccessStatus.OK);
    }
}
