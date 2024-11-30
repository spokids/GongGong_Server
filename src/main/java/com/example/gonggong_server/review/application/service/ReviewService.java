package com.example.gonggong_server.review.application.service;

import com.example.gonggong_server.global.exception.GlobalException;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.global.util.S3Util;
import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.program.domain.repository.ProgramRepository;
import com.example.gonggong_server.program.exception.ProgramException;
import com.example.gonggong_server.review.api.request.ReviewRequestDTO;
import com.example.gonggong_server.review.application.response.MyReviewListResponseDTO;
import com.example.gonggong_server.review.application.response.ReviewListResponseDTO;
import com.example.gonggong_server.review.application.response.ReviewResponseDTO;
import com.example.gonggong_server.review.domain.entity.Review;
import com.example.gonggong_server.review.domain.repository.ReviewRepository;
import com.example.gonggong_server.review.exception.ReviewException;
import com.example.gonggong_server.user.domain.entity.User;
import com.example.gonggong_server.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProgramRepository programRepository;
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

    public ReviewListResponseDTO getReviews(Long programId, Long lastReviewId, int size) {
        Pageable pageable = PageRequest.of(0, size + 1);
        List<Review> reviews = reviewRepository.findReviews(programId, lastReviewId, pageable);

        // 다음 조회할 데이터가 남아있는지 확인
        boolean hasNext = reviews.size() == size + 1;
        if (hasNext)
            reviews = reviews.subList(0, size);

        // 리뷰 작성한 사용자 정보 조회
        List<User> users = userRepository.findAllByUserId(
                reviews.stream().map(Review::getUserId).toList()
        );

        Map<Long, String> userNicknames = users.stream()
                .collect(Collectors.toMap(User::getUserId, User::getNickName));

        List<ReviewListResponseDTO.ReviewDTO> reviewDTOs = reviews.stream()
                .map(review -> ReviewListResponseDTO.ReviewDTO.of(review, userNicknames)).toList();

        return ReviewListResponseDTO.of(reviewDTOs, hasNext);
    }
    public MyReviewListResponseDTO getMyReviews(String userInputId, int size, Long lastReviewId) {
        User user = findUserById(userInputId);
        Pageable pageable = PageRequest.of(0, size + 1);
        List<Review> reviews = reviewRepository.findReviewsByUserId(user.getUserId(), lastReviewId, pageable);

        // 다음 조회할 데이터가 남아있는지 확인
        boolean hasNext = reviews.size() == size + 1;
        if (hasNext)
            reviews = reviews.subList(0, size);

        List<MyReviewListResponseDTO.MyReviewDTO> reviewDTOs = getMyReviewDTOS(reviews);

        return MyReviewListResponseDTO.of(user.getNickName(), userInputId, reviewDTOs, hasNext);
    }

    private List<MyReviewListResponseDTO.MyReviewDTO> getMyReviewDTOS(List<Review> reviewsPage) {
        List<MyReviewListResponseDTO.MyReviewDTO> reviews = reviewsPage.stream().map(
                review -> {
                    Program program = programRepository.findByProgramId(review.getProgramId())
                            .orElseThrow(()-> new ProgramException(ErrorStatus.PROGRAM_NOT_EXIST));
                    return MyReviewListResponseDTO.MyReviewDTO.of(review, program);
                })
                .toList();
        return reviews;
    }

    public void deleteMyReview(String userInputId, Long reviewId){
        User user = findUserById(userInputId);
        Review review = getValidatedReview(reviewId, user);
        reviewRepository.delete(review);
    }

    private Review getValidatedReview(Long reviewId, User user) {
        Review review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new ReviewException(ErrorStatus.REVIEW_NOT_EXIST));

        if (!review.getUserId().equals(user.getUserId())) {
            throw new ReviewException(ErrorStatus.UNAUTHORIZED_REVIEW_ACCESS);
        }

        return review;
    }
}
