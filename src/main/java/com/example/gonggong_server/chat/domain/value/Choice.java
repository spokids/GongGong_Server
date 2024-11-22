package com.example.gonggong_server.chat.domain.value;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Choice {
    FREE_CHAT("자유롭게 아이에게 맞는 프로그램을 찾고 싶어요"),
    ABILITY_CHAT("키우고 싶은 능력치를 기준으로 찾고 싶어요");

    private final String value;

}
