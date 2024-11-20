package com.example.gonggong_server.review.api.request;

import jakarta.validation.constraints.NotNull;

public record ReviewRequestDTO(@NotNull(message = "후기 내용을 입력하세요.") String content) {
}
