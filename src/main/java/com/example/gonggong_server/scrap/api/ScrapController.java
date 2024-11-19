package com.example.gonggong_server.scrap.api;

import com.example.gonggong_server.global.response.ApiResponse;
import com.example.gonggong_server.global.status.SuccessStatus;
import com.example.gonggong_server.scrap.application.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/program/scrap")
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping("/{programId}")
    public ResponseEntity<ApiResponse<String>> scrapProgram(
            @AuthenticationPrincipal String userInputId,
            @PathVariable("programId") Long programId
    ) {
        scrapService.scrapProgram(userInputId, programId);
        return ApiResponse.success(SuccessStatus.CREATED);
    }

    @DeleteMapping("/{programId}")
    public ResponseEntity<ApiResponse<String>> cancelScrapProgram(
            @AuthenticationPrincipal String userInputId,
            @PathVariable("programId") Long programId
    ) {
        scrapService.cancelScrapProgram(userInputId, programId);
        return ApiResponse.success(SuccessStatus.OK);
    }
}
