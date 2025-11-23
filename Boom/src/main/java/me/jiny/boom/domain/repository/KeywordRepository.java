package me.jiny.boom.domain.repository;

import me.jiny.boom.domain.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findByType(Keyword.KeywordType type);
}

