package com.example.gonggong_server.global.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus {
    // 200
    OK(HttpStatus.OK, "OK"),

    // 201
    CREATED(HttpStatus.CREATED, "Created");

    private final HttpStatus httpStatus;
    private final String message;
}
