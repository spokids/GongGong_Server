package com.example.gonggong_server.review.application.service;

import com.example.gonggong_server.global.exception.GlobalException;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.global.util.S3Util;
import com.example.gonggong_server.review.api.request.ReviewRequestDTO;
import com.example.gonggong_server.review.application.response.ReviewListResponseDTO;
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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final S3Util s3Util;

    @Transactional
    public ReviewResponseDTO createReview(String userInputId, Long programId, MultipartFile image, ReviewRequestDTO request) throws IOException {
        String imageUrl = null;
        if(!(image == null)) {
            imageUrl = s3Util.uploadImage(image, "review/" + UUID.randomUUID());
        }

        validateContent(request.content());

        User user = findUserById(userInputId);
        Review review = Review.from(user.getUserId(), programId, request.content(), imageUrl);

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

    public ReviewListResponseDTO getReviews(Long programId) {
        List<Review> reviews = reviewRepository.findAllByProgramId(programId);

        // 리뷰 작성한 사용자 정보 조회
        List<User> users = userRepository.findAllByUserId(
                reviews.stream().map(Review::getUserId).toList()
        );

        Map<Long, String> userNicknames = users.stream()
                .collect(Collectors.toMap(User::getUserId, User::getNickName));

        List<ReviewListResponseDTO.ReviewDTO> reviewDTOs = reviews.stream()
                .map(review -> ReviewListResponseDTO.ReviewDTO.of(review, userNicknames)).toList();

        return ReviewListResponseDTO.of(reviewDTOs);
    }
}
