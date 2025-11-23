package me.jiny.boom.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "카드 간단 정보 응답 DTO")
public class CardSimpleResponse {

    @Schema(description = "카드 ID", example = "1")
    private Long id;

    @Schema(description = "카드 이름", example = "내 최애 K-POP 앨범")
    private String name;

    @Schema(description = "이미지 URL", example = "https://example.com/card.jpg")
    private String imageUrl;

    @Schema(description = "Boom Level (1-5)", example = "5")
    private Integer boomLevel;

    @Schema(description = "BoomUP 수", example = "42")
    private Long boomUpCount;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;
}

