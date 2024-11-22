package com.example.gonggong_server.option.domain.value;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Ability {
    EARTH("지구력"),
    AGILITY("민첩성"),
    COOPER("협동력"),
    POWER("근력"),
    FLEX("유연성"),
    REACTION("반응속도"),
    EXPRESSION("표현력"),
    BALANCE("균형감각"),
    FOCUS("집중력"),
    QUICK("순발력"),
    PRECISION("정밀성");

    private final String value;

}
