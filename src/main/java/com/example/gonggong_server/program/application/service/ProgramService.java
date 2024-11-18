package com.example.gonggong_server.program.application.service;

import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.program.application.response.DongResponseDTO;
import com.example.gonggong_server.program.application.response.SigunguResponseDTO;
import com.example.gonggong_server.program.domain.repository.ProgramRepository;
import com.example.gonggong_server.program.exception.ProgramException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;

    public SigunguResponseDTO getSigunguList(String province) {
        List<String> districtNames = programRepository.findSigunguByProvince(province);

        // 요청한 지역에 대한 시군구 정보가 없을 경우
        validateSigungu(districtNames);

        return SigunguResponseDTO.of(districtNames);
    }

    private static void validateSigungu(List<String> districtNames) {
        if(districtNames.isEmpty()) {
            throw new ProgramException(ErrorStatus.INVALID_PROVINCE);
        }
    }

    public DongResponseDTO getDongList(String province, String sigungu) {
        List<String> dongNames = programRepository.findDongBySigungu(province, sigungu);

        // 요청한 지역에 대한 동 정보가 없을 경우
        validateDong(dongNames);

        return DongResponseDTO.of(dongNames);
    }

    private static void validateDong(List<String> dongNames) {
        if(dongNames.isEmpty()) {
            throw new ProgramException(ErrorStatus.INVALID_SIGUNGU);
        }
    }
}
