package com.example.gonggong_server.review.intra.repository;

import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.review.domain.entity.Review;
import com.example.gonggong_server.review.domain.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaReviewRepository extends ReviewRepository, JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.programId = :programId ORDER BY r.createDate DESC")
    List<Review> findAllByProgramId(Long programId);

    @Query("SELECT DISTINCT p FROM Review r JOIN Program p ON r.programId = p.programId ORDER BY r.createDate DESC")
    Page<Program> findReviewedPrograms(Pageable pageable);
}
