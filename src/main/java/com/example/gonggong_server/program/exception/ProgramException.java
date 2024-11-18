package com.example.gonggong_server.program.exception;

import com.example.gonggong_server.global.status.ErrorStatus;
import lombok.Getter;

@Getter
public class ProgramException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public ProgramException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}
