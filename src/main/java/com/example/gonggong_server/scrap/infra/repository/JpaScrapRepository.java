package com.example.gonggong_server.scrap.infra.repository;

import com.example.gonggong_server.scrap.domain.entity.Scrap;
import com.example.gonggong_server.scrap.domain.repository.ScrapRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaScrapRepository extends ScrapRepository, JpaRepository<Scrap, Long> {
    @Modifying
    @Query("DELETE FROM Scrap s WHERE s.userId = :userId AND s.programId = :programId")
    int deleteByUserIdAndProgramId(@Param("userId") Long userId, @Param("programId") Long programId);
    @Query("SELECT s FROM Scrap s WHERE s.userId = :userId")
    Page<Scrap> findScraps(@Param("userId") Long userId, Pageable pageable);
    @Query("SELECT s.programType FROM Scrap s GROUP BY s.programType ORDER BY COUNT(s.programType) DESC LIMIT 3")
    List<String> findTop3Types();
}
