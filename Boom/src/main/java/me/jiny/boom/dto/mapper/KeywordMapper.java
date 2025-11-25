package me.jiny.boom.dto.mapper;

import me.jiny.boom.domain.entity.Keyword;
import me.jiny.boom.dto.response.KeywordResponse;

public class KeywordMapper {

    public static KeywordResponse toResponse(Keyword keyword) {
        return KeywordResponse.builder()
            .id(keyword.getId())
            .name(keyword.getName())
            .type(keyword.getType())
            .build();
    }
}

