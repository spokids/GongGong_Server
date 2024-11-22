package com.example.gonggong_server.option.infra.repository;

import com.example.gonggong_server.option.domain.entity.Option;
import com.example.gonggong_server.option.domain.repository.OptionRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOptionRepository extends OptionRepository, JpaRepository<Option, Long> {
}
