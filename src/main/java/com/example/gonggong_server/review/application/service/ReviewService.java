package com.example.gonggong_server.review.application.service;

import com.example.gonggong_server.global.exception.GlobalException;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.global.util.S3Util;
import com.example.gonggong_server.review.api.request.ReviewRequestDTO;
import com.example.gonggong_server.review.application.response.ReviewResponseDTO;
import com.example.gonggong_server.review.domain.entity.Review;
import com.example.gonggong_server.review.domain.repository.ReviewRepository;
import com.example.gonggong_server.review.exception.ReviewException;
import com.example.gonggong_server.user.domain.entity.User;
import com.example.gonggong_server.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final S3Util s3Util;

    @Transactional
    public ReviewResponseDTO createReview(String userInputId, Long programId, MultipartFile image, ReviewRequestDTO request) throws IOException {
        String imageUrl = s3Util.uploadImage(image, "review/" + UUID.randomUUID());

        User user = findUserById(userInputId);

        validateContent(request.content());

        Review review = Review.from(user.getUserId(), programId, imageUrl, request.content());

        Long reviewId = reviewRepository.save(review).getReviewId();
        return ReviewResponseDTO.of(reviewId);
    }

    private User findUserById(String userInputId) {
        return userRepository.findByUserInputId(userInputId)
                .orElseThrow(() -> new GlobalException(ErrorStatus.USER_NOT_FOUND));
    }

    private void validateContent(String content) {
        if(content == null || content.isEmpty() || content.length() > 300) {
            throw new ReviewException(ErrorStatus.OVER_REVIEW_CONTENT_LENGTH);
        }
    }
}
