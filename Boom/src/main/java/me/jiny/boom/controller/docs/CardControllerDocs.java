package me.jiny.boom.controller.docs;

import me.jiny.boom.dto.request.CardCreateRequest;
import me.jiny.boom.dto.request.CardUpdateRequest;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.CardResponse;
import me.jiny.boom.dto.response.CardSimpleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Card", description = "카드 관련 API")
public interface CardControllerDocs {

    @Operation(summary = "카드 생성", description = "새로운 Boom! 카드를 생성합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<ApiPayload<CardResponse>> createCard(Authentication authentication, @Valid @RequestBody CardCreateRequest request);

    @Operation(summary = "카드 상세 조회", description = "특정 카드의 상세 정보를 조회합니다.")
    @Parameter(name = "cardId", description = "카드 ID")
    ResponseEntity<ApiPayload<CardResponse>> getCard(@PathVariable Long cardId, @AuthenticationPrincipal Authentication authentication);

    @Operation(summary = "카드 수정", description = "내가 생성한 카드의 정보를 수정합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "cardId", description = "카드 ID")
    ResponseEntity<ApiPayload<CardResponse>> updateCard(Authentication authentication, @PathVariable Long cardId, @Valid @RequestBody CardUpdateRequest request);

    @Operation(summary = "카드 삭제", description = "내가 생성한 카드를 삭제합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "cardId", description = "카드 ID")
    ResponseEntity<ApiPayload<Void>> deleteCard(Authentication authentication, @PathVariable Long cardId);

    @Operation(summary = "내 카드 목록 조회", description = "현재 로그인한 사용자가 생성한 카드 목록을 조회합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<ApiPayload<List<CardSimpleResponse>>> getMyCards(Authentication authentication);

    @Operation(summary = "사용자 카드 목록 조회", description = "특정 사용자가 생성한 카드 목록을 조회합니다.")
    @Parameter(name = "userId", description = "사용자 ID")
    ResponseEntity<ApiPayload<List<CardSimpleResponse>>> getUserCards(@PathVariable Long userId);

    @Operation(summary = "Explore - 최근 카드 조회", description = "최근에 생성된 카드들을 조회합니다.")
    ResponseEntity<ApiPayload<List<CardSimpleResponse>>> getRecentCards();

    @Operation(summary = "Explore - 인기 카드 랭킹", description = "BoomUP 수가 많은 인기 카드 랭킹을 조회합니다.")
    ResponseEntity<ApiPayload<List<CardSimpleResponse>>> getRankingCards();

    @Operation(summary = "Explore - 추천 카드", description = "내 카드와 유사한 카드 4개를 추천받습니다.", security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<ApiPayload<List<CardSimpleResponse>>> getRecommendedCards(Authentication authentication);

    @Operation(summary = "카드 이미지 다운로드", description = "생성한 카드를 이미지 파일로 다운로드합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "cardId", description = "카드 ID")
    byte[] downloadCardImage(@PathVariable Long cardId);
}

