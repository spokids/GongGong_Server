package com.example.gonggong_server.program.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigunguResponseDTO {
    private List<String> sigungu;

    public static SigunguResponseDTO of(List<String> sigungu) {
        return SigunguResponseDTO.builder()
                .sigungu(sigungu)
                .build();
    }
}
