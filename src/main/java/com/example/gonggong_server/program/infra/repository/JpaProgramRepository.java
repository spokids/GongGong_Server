package com.example.gonggong_server.program.infra.repository;

import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.program.domain.repository.ProgramRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaProgramRepository extends ProgramRepository, JpaRepository<Program, Long> {
    @Query("SELECT p FROM Program p " +
            "WHERE :age BETWEEN p.startAge AND p.endAge " +
            "AND p.fullAddress LIKE %:location% " +
            "AND p.type = :type")
    List<Program> findProgramsByCriteria(@Param("age") int age,
                                         @Param("location") String location,
                                         @Param("type") String type);
}
