package me.jiny.boom.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "통계 정보 응답 DTO")
public class StatisticsResponse {

    @Schema(description = "TOP 5 카테고리")
    private List<CategoryStatsResponse> topCategories;

    @Schema(description = "TOP 5 키워드")
    private List<KeywordStatsResponse> topKeywords;

    @Schema(description = "월별 통계")
    private List<MonthlyStatsResponse> monthlyStats;

    @Schema(description = "취향 유형 분석 결과")
    private String tasteType;
}

