package com.example.gonggong_server.program.infra.repository;

import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.program.domain.repository.ProgramRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaProgramRepository extends ProgramRepository, JpaRepository<Program, Long> {

    @Query("SELECT DISTINCT p.districtName FROM Program p WHERE p.provinceName = :province")
    List<String> findSigunguByProvince(@Param("province") String province);

    @Query("SELECT DISTINCT p.subDistrict FROM Program p WHERE p.provinceName = :province AND p.districtName = :sigungu")
    List<String> findDongBySigungu(@Param("province") String province, @Param("sigungu") String sigungu);
}
