package me.jiny.boom.domain.repository;

import me.jiny.boom.domain.entity.Category;
import me.jiny.boom.domain.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findByCategory(Category category);
}

