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
@Schema(description = "키워드 통계 응답 DTO")
public class KeywordStatsResponse {

    @Schema(description = "키워드 정보")
    private KeywordResponse keyword;

    @Schema(description = "사용 횟수", example = "12")
    private Long count;
}

