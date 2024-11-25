package com.example.gonggong_server.program.infra.repository;

import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.program.domain.repository.ProgramRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaProgramRepository extends ProgramRepository, JpaRepository<Program, Long> {
    @Query("SELECT p FROM Program p " +
            "WHERE :age BETWEEN p.startAge AND p.endAge " +
            "AND p.fullAddress LIKE %:location% " +
            "AND p.type = :type")
    Page<Program> findProgramsByCriteria(@Param("age") int age,
                                         @Param("location") String location,
                                         @Param("type") String type, Pageable pageable);

    @Query("SELECT DISTINCT p.districtName FROM Program p WHERE p.provinceName = :province")
    List<String> findSigunguByProvince(@Param("province") String province);

    @Query("SELECT DISTINCT p.subDistrict FROM Program p WHERE p.provinceName = :province AND p.districtName = :sigungu")
    List<String> findDongBySigungu(@Param("province") String province, @Param("sigungu") String sigungu);


    @Query("SELECT p FROM Program p " +
            "WHERE p.provinceName = :province " +
            "AND (:sigungu IS NULL OR p.districtName = :sigungu) " +
            "AND (:dong IS NULL OR p.subDistrict = :dong) " +
            "AND (:age IS NULL OR :age BETWEEN p.startAge AND p.endAge) " +
            "AND (:types IS NULL OR p.type IN :types)")
    Page<Program> findPrograms(
            @Param("province") String province,
            @Param("sigungu") String sigungu,
            @Param("dong") String dong,
            @Param("age") Integer age,
            @Param("types") List<String> types,
            Pageable pageable
    );

    @Query("SELECT p FROM Program p WHERE " +
            "p.ability IN :abilities AND p.fullAddress LIKE %:region%")
    Page<Program> findByAbilitiesAndAddress(
            @Param("abilities") List<String> abilities,
            @Param("region") String region,
            Pageable pageable
    );
}
