package com.example.gonggong_server.review.exception;

import com.example.gonggong_server.global.status.ErrorStatus;
import lombok.Getter;

@Getter
public class ReviewException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public ReviewException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}
