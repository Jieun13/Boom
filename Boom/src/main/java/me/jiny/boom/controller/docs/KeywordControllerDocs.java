package me.jiny.boom.controller.docs;

import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.KeywordResponse;
import me.jiny.boom.domain.entity.Keyword;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Keyword", description = "키워드 관련 API")
public interface KeywordControllerDocs {

    @Operation(summary = "전체 키워드 조회", description = "모든 키워드 목록을 조회합니다.")
    ApiPayload<List<KeywordResponse>> getAllKeywords();

    @Operation(summary = "타입별 키워드 조회", description = "특정 타입의 키워드 목록을 조회합니다. (FEELING, ACTION, TENDENCY)")
    @Parameter(name = "type", description = "키워드 타입")
    ApiPayload<List<KeywordResponse>> getKeywordsByType(@Parameter(hidden = true) Keyword.KeywordType type);
}

