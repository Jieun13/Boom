package me.jiny.boom.controller.docs;

import me.jiny.boom.dto.response.ApiPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "BoomUP", description = "BoomUP(좋아요) 관련 API")
public interface BoomUPControllerDocs {

    @Operation(summary = "BoomUP 추가", description = "카드에 BoomUP(좋아요)을 추가합니다.")
    @Parameter(name = "cardId", description = "카드 ID")
    ApiPayload<Void> addBoomUP(Long cardId);

    @Operation(summary = "BoomUP 취소", description = "카드의 BoomUP(좋아요)을 취소합니다.")
    @Parameter(name = "cardId", description = "카드 ID")
    ApiPayload<Void> removeBoomUP(Long cardId);
}

