package me.jiny.boom.controller;

import me.jiny.boom.controller.docs.KeywordControllerDocs;
import me.jiny.boom.domain.entity.Keyword;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.KeywordResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keywords")
public class KeywordController implements KeywordControllerDocs {

    @Override
    @GetMapping
    public ApiPayload<List<KeywordResponse>> getAllKeywords() {
        return null;
    }

    @Override
    @GetMapping("/type/{type}")
    public ApiPayload<List<KeywordResponse>> getKeywordsByType(@PathVariable Keyword.KeywordType type) {
        return null;
    }
}

