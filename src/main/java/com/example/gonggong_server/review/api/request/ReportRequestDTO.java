package com.example.gonggong_server.review.api.request;

import com.example.gonggong_server.review.domain.value.Report;

import java.util.List;

public record ReportRequestDTO(
        List<Report> reports
) {
}
