package com.example.gonggong_server.user.api.controller;

import com.example.gonggong_server.global.response.ApiResponse;
import com.example.gonggong_server.global.status.SuccessStatus;
import com.example.gonggong_server.user.application.response.UserInfoResponseDTO;
import com.example.gonggong_server.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> userDelete(
            @AuthenticationPrincipal String userInputId
    ) {
        userService.deleteUser(userInputId);
        return ApiResponse.success(SuccessStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserInfoResponseDTO>> getUserInfo(
            @AuthenticationPrincipal String userInputId
    ) {
        UserInfoResponseDTO response = userService.getUserInfo(userInputId);
        return ApiResponse.success(SuccessStatus.OK, response);
    }
}
