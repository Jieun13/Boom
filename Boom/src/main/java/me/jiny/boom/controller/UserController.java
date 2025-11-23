package me.jiny.boom.controller;

import me.jiny.boom.controller.docs.UserControllerDocs;
import me.jiny.boom.dto.request.UserProfileUpdateRequest;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.SimilarityResponse;
import me.jiny.boom.dto.response.StatisticsResponse;
import me.jiny.boom.dto.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController implements UserControllerDocs {

    @Override
    @GetMapping("/me")
    public ApiPayload<UserResponse> getMyProfile() {
        return null;
    }

    @Override
    @PutMapping("/me")
    public ApiPayload<UserResponse> updateMyProfile(@Valid @RequestBody UserProfileUpdateRequest request) {
        return null;
    }

    @Override
    @GetMapping("/{userId}")
    public ApiPayload<UserResponse> getUserProfile(@PathVariable Long userId) {
        return null;
    }

    @Override
    @GetMapping("/{userId}/similarity")
    public ApiPayload<SimilarityResponse> getSimilarity(@PathVariable Long userId) {
        return null;
    }

    @Override
    @GetMapping("/{userId}/statistics")
    public ApiPayload<StatisticsResponse> getStatistics(@PathVariable Long userId) {
        return null;
    }
}

