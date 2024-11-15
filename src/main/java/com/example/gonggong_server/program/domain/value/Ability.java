package com.example.gonggong_server.program.domain.value;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Ability {
    ENDURANCE("지구력"),
    AGILITY("민첩성"),
    TEAMWORK("협동력"),
    STRENGTH("근력"),
    FLEXIBILITY("유연성"),
    REACTION_SPEED("반응속도"),
    EXPRESSIVENESS("표현력"),
    BALANCE("균형감각"),
    FOCUS("집중력"),
    EXPLOSIVENESS("순발력"),
    PRECISION("정밀성");

    private final String value;
}