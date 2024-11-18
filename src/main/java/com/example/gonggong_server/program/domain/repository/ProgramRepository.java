package com.example.gonggong_server.program.domain.repository;

import com.example.gonggong_server.program.domain.entity.Program;

import java.util.List;

public interface ProgramRepository {
    List<Program> findProgramsByCriteria(int age, String location, String type);
}
