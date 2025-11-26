package me.jiny.boom.service;

import lombok.RequiredArgsConstructor;
import me.jiny.boom.domain.entity.Keyword;
import me.jiny.boom.domain.repository.KeywordRepository;
import me.jiny.boom.dto.mapper.KeywordMapper;
import me.jiny.boom.dto.response.KeywordResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional(readOnly = true)
    public List<KeywordResponse> getAll() {
        return keywordRepository.findAll().stream()
            .map(KeywordMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<KeywordResponse> getAllByType(Keyword.KeywordType type) {
        return keywordRepository.findByType(type).stream()
            .map(KeywordMapper::toResponse)
            .collect(Collectors.toList());
    }
}
