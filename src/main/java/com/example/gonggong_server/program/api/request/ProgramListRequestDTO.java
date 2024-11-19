package com.example.gonggong_server.program.api.request;

import lombok.Data;

import java.util.List;

@Data
public class ProgramListRequestDTO {
    private String province;
    private String sigungu;
    private String dong;
    private int age;
    private List<String> types;
}