package me.jiny.boom.dto.mapper;

import me.jiny.boom.domain.entity.Card;
import me.jiny.boom.dto.response.CardResponse;
import me.jiny.boom.dto.response.CardSimpleResponse;

import java.util.stream.Collectors;

public class CardMapper {

    public static CardSimpleResponse toSimpleResponse(Card card) {
        return CardSimpleResponse.builder()
            .id(card.getId())
            .name(card.getName())
            .imageUrl(card.getImageUrl())
            .boomLevel(card.getBoomLevel())
            .boomUpCount(card.getBoomUpCount() != null ? card.getBoomUpCount().longValue() : 0L)
            .createdAt(card.getCreatedAt())
            .build();
    }

    public static CardResponse toResponse(Card card, Long currentUserId) {
        boolean isBoomUpped = currentUserId != null && card.getBoomUps().stream()
            .anyMatch(bu -> bu.getUser().getId().equals(currentUserId));
        boolean isCollected = currentUserId != null && card.getCollections().stream()
            .anyMatch(cc -> cc.getUser().getId().equals(currentUserId));

        return CardResponse.builder()
            .id(card.getId())
            .user(UserMapper.toSimpleResponse(card.getUser()))
            .category(CategoryMapper.toResponse(card.getCategory()))
            .subCategory(card.getSubCategory() != null ? SubCategoryMapper.toResponse(card.getSubCategory()) : null)
            .name(card.getName())
            .description(card.getDescription())
            .boomLevel(card.getBoomLevel())
            .imageUrl(card.getImageUrl())
            .keywords(card.getCardKeywords().stream()
                .map(ck -> KeywordMapper.toResponse(ck.getKeyword()))
                .collect(Collectors.toList()))
            .boomUpCount(card.getBoomUpCount() != null ? card.getBoomUpCount().longValue() : 0L)
            .isBoomUpped(isBoomUpped)
            .isCollected(isCollected)
            .createdAt(card.getCreatedAt())
            .updatedAt(card.getUpdatedAt())
            .build();
    }
}

