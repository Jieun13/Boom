package me.jiny.boom.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "취향 유사도 응답 DTO")
public class SimilarityResponse {

    @Schema(description = "대상 사용자 정보")
    private UserSimpleResponse targetUser;

    @Schema(description = "유사도 점수 (0.0 ~ 1.0)", example = "0.85")
    private Double similarityScore;

    @Schema(description = "유사도 설명", example = "매우 유사한 취향을 가지고 있습니다")
    private String description;
}

