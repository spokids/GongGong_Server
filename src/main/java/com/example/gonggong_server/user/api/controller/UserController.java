package com.example.gonggong_server.user.api.controller;

import com.example.gonggong_server.global.response.ApiResponse;
import com.example.gonggong_server.global.status.SuccessStatus;
import com.example.gonggong_server.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
