package com.example.gonggong_server.scrap.infra.repository;

import com.example.gonggong_server.scrap.domain.entity.Scrap;
import com.example.gonggong_server.scrap.domain.repository.ScrapRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaScrapRepository extends ScrapRepository, JpaRepository<Scrap, Long> {

}
