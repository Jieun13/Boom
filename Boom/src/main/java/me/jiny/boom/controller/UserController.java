package me.jiny.boom.controller;

import lombok.RequiredArgsConstructor;
import me.jiny.boom.controller.docs.UserControllerDocs;
import me.jiny.boom.dto.request.UserProfileUpdateRequest;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.SimilarityResponse;
import me.jiny.boom.dto.response.StatisticsResponse;
import me.jiny.boom.dto.response.UserPublicResponse;
import me.jiny.boom.dto.response.UserResponse;
import me.jiny.boom.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    private final UserService userService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<ApiPayload<UserResponse>> getMyProfile(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiPayload.ok(userService.getMyProfile(userId));
    }

    @Override
    @PutMapping("/me")
    public ResponseEntity<ApiPayload<UserResponse>> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody UserProfileUpdateRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiPayload.ok("프로필이 수정되었습니다", userService.updateMyProfile(userId, request));
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<ApiPayload<UserPublicResponse>> getUserProfile(@PathVariable Long userId) {
        return ApiPayload.ok(userService.getUserProfile(userId));
    }

    @Override
    @GetMapping("/{userId}/similarity")
    public ResponseEntity<ApiPayload<SimilarityResponse>> getSimilarity(
            Authentication authentication,
            @PathVariable Long userId) {
        Long currentUserId = Long.parseLong(authentication.getName());
        return ApiPayload.ok(userService.getSimilarity(currentUserId, userId));
    }

    @Override
    @GetMapping("/{userId}/statistics")
    public ResponseEntity<ApiPayload<StatisticsResponse>> getStatistics(@PathVariable Long userId) {
        return ApiPayload.ok(userService.getStatistics(userId));
    }
}

