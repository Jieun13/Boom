package me.jiny.boom.controller;

import lombok.RequiredArgsConstructor;
import me.jiny.boom.controller.docs.CategoryControllerDocs;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.CategoryResponse;
import me.jiny.boom.dto.response.SubCategoryResponse;
import me.jiny.boom.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerDocs {

    private final CategoryService categoryService;

    @Override
    @GetMapping
    public ApiPayload<List<CategoryResponse>> getAllCategories() {
        return ApiPayload.onSuccess(categoryService.getAll());
    }

    @Override
    @GetMapping("/{categoryId}/subcategories")
    public ApiPayload<List<SubCategoryResponse>> getSubCategoriesByCategory(@PathVariable("categoryId") Long categoryId) {
        return ApiPayload.onSuccess(categoryService.getAllByCategory(categoryId));
    }
}