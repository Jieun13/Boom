package me.jiny.boom.controller;

import lombok.RequiredArgsConstructor;
import me.jiny.boom.controller.docs.KeywordControllerDocs;
import me.jiny.boom.domain.entity.Keyword;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.KeywordResponse;
import me.jiny.boom.service.KeywordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keywords")
@RequiredArgsConstructor
public class KeywordController implements KeywordControllerDocs {

    private final KeywordService keywordService;

    @Override
    @GetMapping
    public ApiPayload<List<KeywordResponse>> getAllKeywords() {
        return ApiPayload.onSuccess(keywordService.getAll());
    }

    @Override
    @GetMapping("/type/{type}")
    public ApiPayload<List<KeywordResponse>> getKeywordsByType(@PathVariable("type") Keyword.KeywordType type) {
        return ApiPayload.onSuccess(keywordService.getAllByType(type));
    }
}