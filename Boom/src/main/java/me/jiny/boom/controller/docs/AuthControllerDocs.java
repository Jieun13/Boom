package me.jiny.boom.controller.docs;

import me.jiny.boom.dto.request.UserLoginRequest;
import me.jiny.boom.dto.request.UserSignUpRequest;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.UserLoginResponse;
import me.jiny.boom.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Auth", description = "회원가입/로그인/로그아웃/인증 관련 API")
public interface AuthControllerDocs {

    @Operation(summary = "회원가입", description = "회원가입 기능입니다.")
    ResponseEntity<ApiPayload<UserResponse>> signup(@Valid @RequestBody UserSignUpRequest request);

    @Operation(summary = "로그인", description = "로그인 기능입니다.")
    ResponseEntity<ApiPayload<UserLoginResponse>> login(@Valid @RequestBody UserLoginRequest request);

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰으로 액세스 토큰을 갱신합니다.")
    @Parameter(name = "Refresh-Token", description = "리프레시 토큰")
    ResponseEntity<ApiPayload<UserLoginResponse>> refresh(@RequestHeader("Refresh-Token") String refreshToken);
}

