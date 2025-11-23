package me.jiny.boom.controller;

import lombok.RequiredArgsConstructor;
import me.jiny.boom.controller.docs.AuthControllerDocs;
import me.jiny.boom.dto.request.UserLoginRequest;
import me.jiny.boom.dto.request.UserSignUpRequest;
import me.jiny.boom.dto.response.ApiPayload;
import me.jiny.boom.dto.response.UserLoginResponse;
import me.jiny.boom.dto.response.UserResponse;
import me.jiny.boom.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @Override
    @PostMapping("/signup")
    public ResponseEntity<ApiPayload<UserResponse>> signup(@Valid @RequestBody UserSignUpRequest request) {
        UserResponse response = authService.signup(request);
        ApiPayload<UserResponse> payload = ApiPayload.<UserResponse>builder()
            .success(true)
            .message("회원가입이 완료되었습니다")
            .data(response)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(payload);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<ApiPayload<UserLoginResponse>> login(@Valid @RequestBody UserLoginRequest request) {
        UserLoginResponse response = authService.login(request);
        ApiPayload<UserLoginResponse> payload = ApiPayload.<UserLoginResponse>builder()
            .success(true)
            .message("로그인 성공")
            .data(response)
            .build();
        return ResponseEntity.ok(payload);
    }

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<ApiPayload<UserLoginResponse>> refresh(@RequestHeader("Refresh-Token") String refreshToken) {
        UserLoginResponse response = authService.refresh(refreshToken);
        ApiPayload<UserLoginResponse> payload = ApiPayload.<UserLoginResponse>builder()
            .success(true)
            .message("토큰이 갱신되었습니다")
            .data(response)
            .build();
        return ResponseEntity.ok(payload);
    }
}
