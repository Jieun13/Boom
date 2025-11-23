package me.jiny.boom.controller.docs;

import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.CategoryResponse;
import me.jiny.boom.dto.response.SubCategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Category", description = "카테고리 관련 API")
public interface CategoryControllerDocs {

    @Operation(summary = "전체 카테고리 조회", description = "모든 카테고리 목록을 조회합니다.")
    ApiPayload<List<CategoryResponse>> getAllCategories();

    @Operation(summary = "카테고리별 소분류 조회", description = "특정 카테고리에 속한 소분류 목록을 조회합니다.")
    @Parameter(name = "categoryId", description = "카테고리 ID")
    ApiPayload<List<SubCategoryResponse>> getSubCategoriesByCategory(@Parameter(hidden = true) Long categoryId);
}

