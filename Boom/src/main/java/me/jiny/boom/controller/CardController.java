package me.jiny.boom.controller;

import lombok.RequiredArgsConstructor;
import me.jiny.boom.controller.docs.CardControllerDocs;
import me.jiny.boom.dto.request.CardCreateRequest;
import me.jiny.boom.dto.request.CardUpdateRequest;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.CardResponse;
import me.jiny.boom.dto.response.CardSimpleResponse;
import me.jiny.boom.service.CardService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController implements CardControllerDocs {

    private final CardService cardService;

    @Override
    @PostMapping
    public ResponseEntity<ApiPayload<CardResponse>> createCard(
            Authentication authentication,
            @Valid @RequestBody CardCreateRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiPayload.ok("카드가 생성되었습니다", cardService.create(request, userId));
    }

    @Override
    @GetMapping("/{cardId}")
    public ResponseEntity<ApiPayload<CardResponse>> getCard(
            @PathVariable("cardId") Long cardId,
            @AuthenticationPrincipal Authentication authentication) {
        Long userId = Optional.ofNullable(authentication)
            .filter(Authentication::isAuthenticated)
            .map(auth -> {
                try {
                    return Long.parseLong(auth.getName());
                } catch (NumberFormatException e) {
                    return null;
                }
            })
            .orElse(null);
        return ApiPayload.ok(cardService.getById(cardId, userId));
    }

    @Override
    @PutMapping("/{cardId}")
    public ResponseEntity<ApiPayload<CardResponse>> updateCard(
            Authentication authentication,
            @PathVariable("cardId") Long cardId,
            @Valid @RequestBody CardUpdateRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiPayload.ok("카드가 수정되었습니다", cardService.update(cardId, request, userId));
    }

    @Override
    @DeleteMapping("/{cardId}")
    public ResponseEntity<ApiPayload<Void>> deleteCard(
            Authentication authentication,
            @PathVariable("cardId") Long cardId) {
        Long userId = Long.parseLong(authentication.getName());
        cardService.delete(cardId, userId);
        return ApiPayload.ok("카드가 삭제되었습니다", null);
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity<ApiPayload<List<CardSimpleResponse>>> getMyCards(
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiPayload.ok(cardService.getUserCards(userId));
    }

    @Override
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiPayload<List<CardSimpleResponse>>> getUserCards(
            @PathVariable("userId") Long userId) {
        return ApiPayload.ok(cardService.getUserCards(userId));
    }

    @Override
    @GetMapping("/explore/recent")
    public ResponseEntity<ApiPayload<List<CardSimpleResponse>>> getRecentCards() {
        return ApiPayload.ok(cardService.getRecentCards());
    }

    @Override
    @GetMapping("/explore/ranking")
    public ResponseEntity<ApiPayload<List<CardSimpleResponse>>> getRankingCards() {
        return ApiPayload.ok(cardService.getRankingCards());
    }

    @Override
    @GetMapping("/explore/recommendations")
    public ResponseEntity<ApiPayload<List<CardSimpleResponse>>> getRecommendedCards(
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiPayload.ok(cardService.getRecommendedCards(userId));
    }

    @Override
    @GetMapping("/{cardId}/download")
    public byte[] downloadCardImage(@PathVariable("cardId") Long cardId) {
        // TODO: 이미지 다운로드 구현? 그냥 프론트에서 처리할까?
        return null;
    }
}
