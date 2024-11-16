package com.example.gonggong_server.program.domain.value;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type {
    SWIMMING("수영"),
    BADMINTON("배드민턴"),
    BASKETBALL("농구"),
    ETC("기타"),
    FITNESS("헬스"),
    AEROBICS("에어로빅"),
    TABLE_TENNIS("탁구"),
    DANCE("댄스"),
    YOGA("요가"),
    BALLET("발레"),
    PILATES("필라테스"),
    ICE_SKATING("빙상"),
    GOLF("골프"),
    SPINNING("스피닝"),
    SQUASH("스쿼시"),
    VOLLEYBALL("배구"),
    SOCCER("축구"),
    KENDO("검도"),
    DANCING("무용"),
    POSTURE_CORRECTION("자세교정"),
    PIANO("피아노"),
    INLINE_SKATING("인라인"),
    JUDO("유도"),
    BOWLING("볼링"),
    MUSIC_JUMP_ROPE("음악줄넘기"),
    TENNIS("테니스");

    private final String value;
}