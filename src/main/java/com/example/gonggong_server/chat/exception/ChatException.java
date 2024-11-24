package com.example.gonggong_server.chat.exception;

import com.example.gonggong_server.global.status.ErrorStatus;
import lombok.Getter;

@Getter
public class ChatException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public ChatException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}

