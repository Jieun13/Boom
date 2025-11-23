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
@Schema(description = "카테고리 통계 응답 DTO")
public class CategoryStatsResponse {

    @Schema(description = "카테고리 정보")
    private CategoryResponse category;

    @Schema(description = "사용 횟수", example = "15")
    private Long count;
}

