package com.example.gonggong_server.review.api.request;

import jakarta.validation.constraints.NotNull;

public record ReviewRequestDTO(@NotNull String content) {
}
