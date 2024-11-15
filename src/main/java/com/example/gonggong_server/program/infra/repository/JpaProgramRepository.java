package com.example.gonggong_server.program.infra.repository;

import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.program.domain.repository.ProgramRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProgramRepository extends ProgramRepository, JpaRepository<Program, Long> {
}
