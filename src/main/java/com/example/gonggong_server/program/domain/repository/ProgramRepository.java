package com.example.gonggong_server.program.domain.repository;

import com.example.gonggong_server.program.domain.entity.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository {
    List<String> findSigunguByProvince(String province);
    List<String> findDongBySigungu(String province, String district);
    Page<Program> findPrograms(String province, String district, String dong, Integer age, List<String> types, Pageable pageable);
    Optional<Program> findByProgramId(Long programId);
    Page<Program> findProgramsByCriteria(int age, String location, String type, Pageable pageable);

    Page<Program> findByAbilitiesAndAddress(List<String> abilityValues, String region, Pageable pageable);
    Page<Program> searchProgramsWithFullText(String province, List<String> abilityValues, String fullAddress,Pageable pageable);
}
