package com.example.gonggong_server.chat.api.request;

import com.example.gonggong_server.option.domain.value.Ability;

import java.util.List;

public record ChatAbilityRequestDTO(Long chatRoomId, List<Ability> abilities, String region) {
}
