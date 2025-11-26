package me.jiny.boom.controller;

import lombok.RequiredArgsConstructor;
import me.jiny.boom.controller.docs.CollectionControllerDocs;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.CardSimpleResponse;
import me.jiny.boom.service.CollectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController implements CollectionControllerDocs {

    private final CollectionService collectionService;

    @Override
    @PostMapping("/cards/{cardId}")
    public ResponseEntity<ApiPayload<Void>> addCollection(
            Authentication authentication,
            @PathVariable Long cardId) {
        Long userId = Long.parseLong(authentication.getName());
        collectionService.addCollection(cardId, userId);
        return ApiPayload.ok("카드가 수집함에 추가되었습니다", null);
    }

    @Override
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<ApiPayload<Void>> removeCollection(
            Authentication authentication,
            @PathVariable Long cardId) {
        Long userId = Long.parseLong(authentication.getName());
        collectionService.removeCollection(cardId, userId);
        return ApiPayload.ok("카드가 수집함에서 제거되었습니다", null);
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity<ApiPayload<List<CardSimpleResponse>>> getMyCollections(
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiPayload.ok(collectionService.getMyCollections(userId));
    }
}

