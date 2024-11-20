package com.example.gonggong_server.scrap.domain.repository;

import com.example.gonggong_server.scrap.domain.entity.Scrap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScrapRepository {
    Scrap save(Scrap scrap);
    Boolean existsByUserIdAndProgramId(Long userId, Long programId);
    int deleteByUserIdAndProgramId(Long userId, Long programId);
    Page<Scrap> findScraps(Long userId, Pageable pageable);
}
