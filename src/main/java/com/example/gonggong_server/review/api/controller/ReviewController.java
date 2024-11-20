package com.example.gonggong_server.review.api.controller;

import com.example.gonggong_server.global.response.ApiResponse;
import com.example.gonggong_server.global.status.SuccessStatus;
import com.example.gonggong_server.review.api.request.ReviewRequestDTO;
import com.example.gonggong_server.review.application.response.ReviewResponseDTO;
import com.example.gonggong_server.review.application.service.ReviewService;
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

    @PostMapping("/{programId}")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> createReview(
            @AuthenticationPrincipal String userInputId,
            @PathVariable Long programId,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "request") ReviewRequestDTO request
    ) throws IOException {
        ReviewResponseDTO response = reviewService.createReview(userInputId, programId, image, request);
        return ApiResponse.success(SuccessStatus.OK, response);

    }
}