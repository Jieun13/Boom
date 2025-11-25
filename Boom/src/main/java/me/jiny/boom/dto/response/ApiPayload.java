package me.jiny.boom.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "공통 API 응답 DTO")
public class ApiPayload<T> {

    @Schema(description = "성공 여부", example = "true")
    private Boolean success;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다")
    private String message;

    @Schema(description = "응답 데이터")
    private T data;

    public static <T> ApiPayload<T> onSuccess(T data) {
        return ApiPayload.<T>builder()
            .success(true)
            .message("요청이 성공적으로 처리되었습니다")
            .data(data)
            .build();
    }

    public static <T> ApiPayload<T> onSuccess(String message, T data) {
        return ApiPayload.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .build();
    }

    public static <T> ResponseEntity<ApiPayload<T>> ok(T data) {
        return ResponseEntity.ok(onSuccess(data));
    }

    public static <T> ResponseEntity<ApiPayload<T>> ok(String message, T data) {
        return ResponseEntity.ok(onSuccess(message, data));
    }

    public static <T> ResponseEntity<ApiPayload<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(onSuccess("생성이 완료되었습니다", data));
    }

    public static <T> ResponseEntity<ApiPayload<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(onSuccess(message, data));
    }
}

