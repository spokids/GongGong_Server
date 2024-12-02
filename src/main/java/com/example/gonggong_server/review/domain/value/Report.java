package com.example.gonggong_server.review.domain.value;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Report {
    ABUSE("욕설/비방을 포함하고 있어요"),
    ADVERTISE("상업적 광고나 부적절한 홍보 글이에요"),
    FAKE("허위 사실 및 잘못된 정보가 포함되어 있어요"),
    SEXUAL("성적으로 불쾌감을 주는 내용이 있어요"),
    EXCHANGE("불법적인 활동이나 거래를 조장하는 글이에요"),
    OTHER("기타 부적절한 내용");

    private final String value;
}
