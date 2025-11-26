package me.jiny.boom.controller.docs;

import me.jiny.boom.dto.request.UserProfileUpdateRequest;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.SimilarityResponse;
import me.jiny.boom.dto.response.StatisticsResponse;
import me.jiny.boom.dto.response.UserPublicResponse;
import me.jiny.boom.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User", description = "사용자 관련 API")
public interface UserControllerDocs {

    @Operation(summary = "내 프로필 조회", description = "현재 로그인한 사용자의 프로필 정보를 조회합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<ApiPayload<UserResponse>> getMyProfile(Authentication authentication);

    @Operation(summary = "프로필 수정", description = "현재 로그인한 사용자의 프로필 정보를 수정합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    ResponseEntity<ApiPayload<UserResponse>> updateMyProfile(Authentication authentication, @Valid @RequestBody UserProfileUpdateRequest request);

    @Operation(summary = "다른 사용자 프로필 조회", description = "특정 사용자의 프로필 정보를 조회합니다. (인증 불필요)")
    @Parameter(name = "userId", description = "사용자 ID")
    ResponseEntity<ApiPayload<UserPublicResponse>> getUserProfile(@PathVariable Long userId);

    @Operation(summary = "취향 유사도 조회", description = "다른 사용자와의 취향 유사도를 조회합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @Parameter(name = "userId", description = "대상 사용자 ID")
    ResponseEntity<ApiPayload<SimilarityResponse>> getSimilarity(Authentication authentication, @PathVariable Long userId);

    @Operation(summary = "취향 통계 조회", description = "사용자의 취향 통계 정보를 조회합니다.")
    @Parameter(name = "userId", description = "사용자 ID")
    ResponseEntity<ApiPayload<StatisticsResponse>> getStatistics(@PathVariable Long userId);
}

