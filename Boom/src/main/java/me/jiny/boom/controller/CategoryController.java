package me.jiny.boom.controller;

import me.jiny.boom.controller.docs.CategoryControllerDocs;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.CategoryResponse;
import me.jiny.boom.dto.response.SubCategoryResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController implements CategoryControllerDocs {

    @Override
    @GetMapping
    public ApiPayload<List<CategoryResponse>> getAllCategories() {
        return null;
    }

    @Override
    @GetMapping("/{categoryId}/subcategories")
    public ApiPayload<List<SubCategoryResponse>> getSubCategoriesByCategory(@PathVariable Long categoryId) {
        return null;
    }
}

