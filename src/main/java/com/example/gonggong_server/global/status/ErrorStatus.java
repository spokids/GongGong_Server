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
    INVALID_PROVINCE(HttpStatus.BAD_REQUEST, "잘못된 특별시/광역시/시 요청입니다."),
    INVALID_SIGUNGU(HttpStatus.BAD_REQUEST, "잘못된 시/군/구 요청입니다."),
    ALREADY_SCRAPPED(HttpStatus.BAD_REQUEST, "이미 스크랩한 프로그램입니다."),
    NOT_SCRAPPED(HttpStatus.BAD_REQUEST, "스크랩하지 않은 프로그램입니다."),
    OVER_REVIEW_CONTENT_LENGTH(HttpStatus.BAD_REQUEST, "리뷰 내용은 300자 이하로 작성해주세요."),

    // 401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다"),

    // 403
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),

    // 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    NO_PROGRAMS_FOUND(HttpStatus.NOT_FOUND, "조건에 맞는 프로그램이 존재하지 않습니다."),
    PROGRAM_NOT_EXIST(HttpStatus.NOT_FOUND, "프로그램이 존재하지 않습니다"),

    // 405
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed"),

    //409
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 유저입니다"),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 오류가 발생했습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
