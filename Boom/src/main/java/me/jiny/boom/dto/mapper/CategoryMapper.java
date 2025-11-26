package me.jiny.boom.dto.mapper;

import me.jiny.boom.domain.entity.Category;
import me.jiny.boom.dto.response.CategoryResponse;

public class CategoryMapper {

    public static CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .build();
    }
}

