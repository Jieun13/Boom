package me.jiny.boom.service;

import lombok.RequiredArgsConstructor;
import me.jiny.boom.domain.entity.Category;
import me.jiny.boom.domain.repository.CategoryRepository;
import me.jiny.boom.domain.repository.SubCategoryRepository;
import me.jiny.boom.dto.mapper.CategoryMapper;
import me.jiny.boom.dto.mapper.SubCategoryMapper;
import me.jiny.boom.dto.response.CategoryResponse;
import me.jiny.boom.dto.response.SubCategoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public List<CategoryResponse> getAll (){
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<SubCategoryResponse> getAllByCategory (Long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NoSuchElementException("Category not found with id: " + categoryId));
        return subCategoryRepository.findByCategory(category).stream()
                .map(SubCategoryMapper::toResponse)
                .collect(Collectors.toList());
    }
}