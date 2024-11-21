package com.example.gonggong_server.review.domain.repository;

import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.review.domain.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepository {
    void deleteByUserId(Long userId);
    Review save(Review review);
    List<Review> findAllByProgramId(Long programId);
    Page<Program> findReviewedPrograms(Pageable pageable);
}
