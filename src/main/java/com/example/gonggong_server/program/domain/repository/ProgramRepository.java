package com.example.gonggong_server.program.domain.repository;

import java.util.List;

public interface ProgramRepository {
    List<String> findDistinctDistrictByProvince(String province);
}
