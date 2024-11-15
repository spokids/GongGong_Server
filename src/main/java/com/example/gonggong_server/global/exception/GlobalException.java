package com.example.gonggong_server.global.exception;

import com.example.gonggong_server.global.status.ErrorStatus;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException{
    private final ErrorStatus errorStatus;

    public GlobalException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}


