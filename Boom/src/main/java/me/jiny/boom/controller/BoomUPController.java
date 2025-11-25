package me.jiny.boom.controller;

import lombok.RequiredArgsConstructor;
import me.jiny.boom.controller.docs.BoomUPControllerDocs;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.service.BoomUPService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boomups")
@RequiredArgsConstructor
public class BoomUPController implements BoomUPControllerDocs {

    private final BoomUPService boomUPService;

    @Override
    @PostMapping("/cards/{cardId}")
    public ResponseEntity<ApiPayload<Void>> addBoomUP(
            Authentication authentication,
            @PathVariable Long cardId) {
        Long userId = Long.parseLong(authentication.getName());
        boomUPService.addBoomUP(cardId, userId);
        return ApiPayload.ok("BoomUP이 추가되었습니다", null);
    }

    @Override
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<ApiPayload<Void>> removeBoomUP(
            Authentication authentication,
            @PathVariable Long cardId) {
        Long userId = Long.parseLong(authentication.getName());
        boomUPService.removeBoomUP(cardId, userId);
        return ApiPayload.ok("BoomUP이 취소되었습니다", null);
    }
}

