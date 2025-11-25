package me.jiny.boom.dto.mapper;

import me.jiny.boom.domain.entity.SubCategory;
import me.jiny.boom.dto.response.SubCategoryResponse;

public class SubCategoryMapper {

    public static SubCategoryResponse toResponse(SubCategory subCategory) {
        return SubCategoryResponse.builder()
            .id(subCategory.getId())
            .name(subCategory.getName())
            .build();
    }
}

