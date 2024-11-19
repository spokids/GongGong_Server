package com.example.gonggong_server.program.domain.repository;

import com.example.gonggong_server.program.domain.entity.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProgramRepository {
    List<String> findSigunguByProvince(String province);
    List<String> findDongBySigungu(String province, String district);
    Page<Program> findPrograms(String province, String district, String dong, Integer age, List<String> types, Pageable pageable);
}
