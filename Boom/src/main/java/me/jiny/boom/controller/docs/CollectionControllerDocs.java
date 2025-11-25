package me.jiny.boom.controller.docs;

import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.CardSimpleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Collection", description = "카드 수집 관련 API")
public interface CollectionControllerDocs {

    @Operation(summary = "카드 수집", description = "카드를 내 수집함에 추가합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "cardId", description = "카드 ID")
    ResponseEntity<ApiPayload<Void>> addCollection(Authentication authentication, @PathVariable Long cardId);

    @Operation(summary = "카드 수집 취소", description = "수집한 카드를 수집함에서 제거합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "cardId", description = "카드 ID")
    ResponseEntity<ApiPayload<Void>> removeCollection(Authentication authentication, @PathVariable Long cardId);

    @Operation(summary = "내 수집함 조회", description = "현재 로그인한 사용자가 수집한 카드 목록을 조회합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<ApiPayload<List<CardSimpleResponse>>> getMyCollections(Authentication authentication);
}

