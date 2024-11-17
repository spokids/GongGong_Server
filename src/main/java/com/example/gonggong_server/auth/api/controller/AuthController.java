package com.example.gonggong_server.auth.api.controller;

import com.example.gonggong_server.auth.api.request.LoginRequestDTO;
import com.example.gonggong_server.auth.api.request.RegisterRequestDTO;
import com.example.gonggong_server.auth.application.response.LoginResponseDTO;
import com.example.gonggong_server.auth.application.service.AuthService;
import com.example.gonggong_server.global.response.ApiResponse;
import com.example.gonggong_server.global.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        authService.register(registerRequestDTO);
        return ApiResponse.success(SuccessStatus.OK);
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = authService.login(loginRequestDTO);
        return ApiResponse.success(SuccessStatus.OK, response);
    }
}