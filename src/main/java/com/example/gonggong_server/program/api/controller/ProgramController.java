package com.example.gonggong_server.program.api.controller;

import com.example.gonggong_server.global.response.ApiResponse;
import com.example.gonggong_server.global.status.SuccessStatus;
import com.example.gonggong_server.program.api.request.ProgramListRequestDTO;
import com.example.gonggong_server.program.application.response.DongResponseDTO;
import com.example.gonggong_server.program.application.response.ProgramDetailResponseDTO;
import com.example.gonggong_server.program.application.response.ProgramListResponseDTO;
import com.example.gonggong_server.program.application.response.SigunguResponseDTO;
import com.example.gonggong_server.program.application.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/program")
public class ProgramController {

    private final ProgramService programService;

    @GetMapping("/sigungu")
    public ResponseEntity<ApiResponse<SigunguResponseDTO>> getSigunguList(
            @RequestParam("province") String province
    ) {
        SigunguResponseDTO response = programService.getSigunguList(province);
        return ApiResponse.success(SuccessStatus.OK, response);
    }

    @GetMapping("/dong")
    public ResponseEntity<ApiResponse<DongResponseDTO>> getDongList(
            @RequestParam("province") String province,
            @RequestParam("sigungu") String sigungu
    ) {
        DongResponseDTO response = programService.getDongList(province, sigungu);
        return ApiResponse.success(SuccessStatus.OK, response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProgramListResponseDTO>> getProgramList(
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestBody ProgramListRequestDTO request
    ) {
        ProgramListResponseDTO response = programService.getProgramList(size, page, request);
        return ApiResponse.success(SuccessStatus.OK, response);
    }

    @GetMapping("/{programId}")
    public ResponseEntity<ApiResponse<ProgramDetailResponseDTO>> getProgramDetail(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long programId
    ) {
        ProgramDetailResponseDTO response = programService.getProgramDetail(programId, token);
        return ApiResponse.success(SuccessStatus.OK, response);
    }

    @GetMapping("/top3")
    public ResponseEntity<ApiResponse<List<String>>> getTop3Types() {
        List<String> response = programService.getTop3Types();
        return ApiResponse.success(SuccessStatus.OK, response);
    }

    @GetMapping("/reviewed")
    public ResponseEntity<ApiResponse<ProgramListResponseDTO>> getReviewedPrograms(
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        ProgramListResponseDTO response = programService.getReviewedPrograms(size, page);
        return ApiResponse.success(SuccessStatus.OK, response);
    }

}