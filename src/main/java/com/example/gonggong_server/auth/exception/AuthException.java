package com.example.gonggong_server.auth.exception;

import com.example.gonggong_server.global.status.ErrorStatus;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public AuthException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}
