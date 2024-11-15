package com.example.gonggong_server.program.domain.value;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProvinceName {
    SEOUL("서울특별시"),
    GYEONGBUK("경상북도"),
    GYEONGGI("경기도"),
    GANGWON("강원특별자치도"),
    BUSAN("부산광역시"),
    DAEGU("대구광역시"),
    INCHEON("인천광역시"),
    CHUNGNAM("충청남도"),
    GYEONGNAM("경상남도"),
    CHUNGBUK("충청북도");

    private final String value;
}