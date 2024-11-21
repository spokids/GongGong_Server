package com.example.gonggong_server.program.application.service;

import com.example.gonggong_server.auth.exception.AuthException;
import com.example.gonggong_server.global.jwt.JWTUtil;
import com.example.gonggong_server.global.status.ErrorStatus;
import com.example.gonggong_server.program.api.request.ProgramListRequestDTO;
import com.example.gonggong_server.program.application.response.DongResponseDTO;
import com.example.gonggong_server.program.application.response.ProgramDetailResponseDTO;
import com.example.gonggong_server.program.application.response.ProgramListResponseDTO;
import com.example.gonggong_server.program.application.response.SigunguResponseDTO;
import com.example.gonggong_server.program.domain.entity.Program;
import com.example.gonggong_server.program.domain.repository.ProgramRepository;
import com.example.gonggong_server.program.exception.ProgramException;
import com.example.gonggong_server.review.domain.entity.Review;
import com.example.gonggong_server.review.domain.repository.ReviewRepository;
import com.example.gonggong_server.scrap.domain.repository.ScrapRepository;
import com.example.gonggong_server.user.domain.entity.User;
import com.example.gonggong_server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final JWTUtil jwtUtil;

    /**
     * 도/특별시에 해당하는 시군구 정보 조회
     * @param province
     * @return
     */
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

    /**
     * 조건에 맞는 프로그램 목록 조회
     * @param pageSize
     * @param pageIndex
     * @param request
     * @return
     */
    public ProgramListResponseDTO getProgramList(int pageSize, int pageIndex, ProgramListRequestDTO request) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);

        Page<Program> programs = programRepository.findPrograms(
                request.getProvince(),
                request.getSigungu(),
                request.getDong(),
                request.getAge() > 0 ? request.getAge() : null,
                (request.getTypes() == null || request.getTypes().isEmpty()) ? null : request.getTypes(),
                pageable
        );

        // 조회된 프로그램이 없을 경우
        if (programs.isEmpty()) {
            throw new ProgramException(ErrorStatus.NO_PROGRAMS_FOUND);
        }

        // 응답 데이터 생성
        List<ProgramListResponseDTO.ProgramDTO> programList = mapProgramsToDTOs(programs);
        ProgramListResponseDTO.ConditionDTO condition = createConditionDTO(request, programs);

        return ProgramListResponseDTO.of(programList, List.of(condition), programs.getTotalPages(), programs.getNumber() + 1);
    }

    private List<ProgramListResponseDTO.ProgramDTO> mapProgramsToDTOs(Page<Program> programs) {
        return programs.stream()
                .map(program -> ProgramListResponseDTO.ProgramDTO.of(
                        program.getProgramId(),
                        program.getProgramName(),
                        program.getFacultyName(),
                        program.getStartAge(),
                        program.getEndAge(),
                        program.getProgramStartDate(),
                        program.getProgramEndDate()
                ))
                .collect(Collectors.toList());
    }

    private ProgramListResponseDTO.ConditionDTO createConditionDTO(ProgramListRequestDTO request, Page<Program> programs) {
        List<String> existingTypes = extractExistingTypes(programs, request.getTypes());
        List<String> notExistingTypes = extractNotExistingTypes(request.getTypes(), existingTypes);

        String age = request.getAge() > 0 ? String.valueOf(request.getAge()) : null;
        String location = String.format("%s %s %s", request.getProvince(), request.getSigungu(), request.getDong());
        return ProgramListResponseDTO.ConditionDTO.of(location, age, existingTypes, notExistingTypes);
    }

    private List<String> extractExistingTypes(Page<Program> programs, List<String> requestedTypes) {
        if (requestedTypes == null || requestedTypes.isEmpty()) {
            return new ArrayList<>();
        }

        return programs.stream()
                .map(Program::getType)
                .distinct()
                .filter(requestedTypes::contains)
                .collect(Collectors.toList());
    }

    private List<String> extractNotExistingTypes(List<String> requestedTypes, List<String> existingTypes) {
        if (requestedTypes == null || requestedTypes.isEmpty()) {
            return new ArrayList<>();
        }

        return requestedTypes.stream()
                .filter(type -> !existingTypes.contains(type))
                .collect(Collectors.toList());
    }


    /**
     * 프로그램 상세 조회
     * @param programId
     * @return
     */
    public ProgramDetailResponseDTO getProgramDetail(Long programId, String token) {
        Program program = findProgramById(programId);
        List<Review> reviews = reviewRepository.findAllByProgramId(programId);

        // 사용자가 스크랩한 프로그램인지 확인
        Boolean isScrapped = false;
        if (token != null) {
            String userInputId = jwtUtil.getUserInputId(token.split(" ")[1]);
            User user = findUserById(userInputId);
            isScrapped = scrapRepository.existsByUserIdAndProgramId(user.getUserId(), programId);
        }

        return ProgramDetailResponseDTO.of(program, reviews.size(), isScrapped);
    }

    private User findUserById(String userInputId) {
        return userRepository.findByUserInputId(userInputId)
                .orElseThrow(() -> new AuthException(ErrorStatus.UNAUTHORIZED));
    }

    private Program findProgramById(Long programId) {
        return programRepository.findByProgramId(programId)
                .orElseThrow(() -> new ProgramException(ErrorStatus.PROGRAM_NOT_EXIST));
    }

    /**
     * 스크랩이 가장 많은 상위 3개 프로그램 타입 조회
     * @return
     */
    public List<String> getTop3Types() {
        return scrapRepository.findTop3Types();
    }
}
