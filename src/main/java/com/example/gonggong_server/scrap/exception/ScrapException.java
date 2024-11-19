package com.example.gonggong_server.scrap.exception;

import com.example.gonggong_server.global.status.ErrorStatus;
import lombok.Getter;

@Getter
public class ScrapException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public ScrapException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}