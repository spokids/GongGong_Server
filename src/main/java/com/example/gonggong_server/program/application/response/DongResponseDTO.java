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
public class DongResponseDTO {
    private List<String> dong;

    public static DongResponseDTO of(List<String> dong) {
        return DongResponseDTO.builder()
                .dong(dong)
                .build();
    }
}
