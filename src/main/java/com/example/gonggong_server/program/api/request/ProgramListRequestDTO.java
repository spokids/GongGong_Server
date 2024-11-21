package com.example.gonggong_server.program.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProgramListRequestDTO {
    @NotNull(message = "도/특별시를 입력해주세요.")
    private String province;
    private String sigungu;
    private String dong;
    private int age;
    private List<String> types;
}