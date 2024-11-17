package com.example.gonggong_server.global.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),
    INVALID_AUTHORIZATION_HEADER(HttpStatus.BAD_REQUEST, "헤더 정보가 잘못됐습니다"),

    // 401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다"),

    // 403
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),

    // 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),

    // 405
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed"),

    //409
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 유저입니다"),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 오류가 발생했습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
