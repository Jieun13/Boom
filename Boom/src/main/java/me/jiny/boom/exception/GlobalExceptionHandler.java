package me.jiny.boom.exception;

import me.jiny.boom.dto.response.ApiPayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ApiPayload<Void>> handleAuthenticationException(AuthenticationCredentialsNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiPayload.<Void>builder()
                .success(false)
                .message(e.getMessage())
                .data(null)
                .build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiPayload<Void>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiPayload.<Void>builder()
                .success(false)
                .message(e.getMessage())
                .data(null)
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiPayload<Void>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiPayload.<Void>builder()
                .success(false)
                .message("서버 오류가 발생했습니다")
                .data(null)
                .build());
    }
}

