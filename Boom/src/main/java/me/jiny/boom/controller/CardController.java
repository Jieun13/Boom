package me.jiny.boom.controller;

import me.jiny.boom.controller.docs.CardControllerDocs;
import me.jiny.boom.dto.request.CardCreateRequest;
import me.jiny.boom.dto.request.CardUpdateRequest;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.CardResponse;
import me.jiny.boom.dto.response.CardSimpleResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController implements CardControllerDocs {

    @Override
    @PostMapping
    public ApiPayload<CardResponse> createCard(@Valid @RequestBody CardCreateRequest request) {
        return null;
    }

    @Override
    @GetMapping("/{cardId}")
    public ApiPayload<CardResponse> getCard(@PathVariable Long cardId) {
        return null;
    }

    @Override
    @PutMapping("/{cardId}")
    public ApiPayload<CardResponse> updateCard(@PathVariable Long cardId, @Valid @RequestBody CardUpdateRequest request) {
        return null;
    }

    @Override
    @DeleteMapping("/{cardId}")
    public ApiPayload<Void> deleteCard(@PathVariable Long cardId) {
        return null;
    }

    @Override
    @GetMapping("/me")
    public ApiPayload<Page<CardSimpleResponse>> getMyCards(Pageable pageable) {
        return null;
    }

    @Override
    @GetMapping("/users/{userId}")
    public ApiPayload<Page<CardSimpleResponse>> getUserCards(@PathVariable Long userId, Pageable pageable) {
        return null;
    }

    @Override
    @GetMapping("/explore/recent")
    public ApiPayload<Page<CardSimpleResponse>> getRecentCards(Pageable pageable) {
        return null;
    }

    @Override
    @GetMapping("/explore/ranking")
    public ApiPayload<Page<CardSimpleResponse>> getRankingCards(Pageable pageable) {
        return null;
    }

    @Override
    @GetMapping("/explore/recommendations")
    public ApiPayload<Page<CardSimpleResponse>> getRecommendedCards(Pageable pageable) {
        return null;
    }

    @Override
    @GetMapping("/{cardId}/download")
    public byte[] downloadCardImage(@PathVariable Long cardId) {
        return null;
    }
}

