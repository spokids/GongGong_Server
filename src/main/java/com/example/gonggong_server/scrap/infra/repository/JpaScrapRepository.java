package com.example.gonggong_server.scrap.infra.repository;

import com.example.gonggong_server.scrap.domain.entity.Scrap;
import com.example.gonggong_server.scrap.domain.repository.ScrapRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaScrapRepository extends ScrapRepository, JpaRepository<Scrap, Long> {
    @Modifying
    @Query("DELETE FROM Scrap s WHERE s.userId = :userId AND s.programId = :programId")
    int deleteByUserIdAndProgramId(@Param("userId") Long userId, @Param("programId") Long programId);
}
