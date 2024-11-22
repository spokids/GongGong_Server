package com.example.gonggong_server.chat.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @NotNull
    private Long chatRoomId;

    @NotNull
    private Boolean author;

    private String content;

    @Lob
    private String currentCriteria;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    public static Chat of(Long chatRoomId, Boolean author, String content, String currentCriteria) {
        Chat chat = new Chat();
        chat.chatRoomId = chatRoomId;
        chat.author = author;
        chat.content = content;
        chat.currentCriteria = currentCriteria;
        chat.createDate = LocalDateTime.now(); // 생성 시점
        return chat;
    }
}
