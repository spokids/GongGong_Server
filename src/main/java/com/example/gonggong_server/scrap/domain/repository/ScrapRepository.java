package com.example.gonggong_server.scrap.domain.repository;

import com.example.gonggong_server.scrap.domain.entity.Scrap;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScrapRepository {
    Scrap save(Scrap scrap);
    Boolean existsByUserIdAndProgramId(Long userId, Long programId);
    int deleteByUserIdAndProgramId(Long userId, Long programId);
    List<Scrap> findScraps(Long userId, Long lastScrapId, Pageable pageable);
    void deleteByUserId(Long userId);
    List<String> findTop3Types();
}
