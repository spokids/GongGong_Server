package com.example.gonggong_server.scrap.domain.repository;

import com.example.gonggong_server.scrap.domain.entity.Scrap;

public interface ScrapRepository {
    Scrap save(Scrap scrap);
    Boolean existsByUserIdAndProgramId(Long userId, Long programId);
}
