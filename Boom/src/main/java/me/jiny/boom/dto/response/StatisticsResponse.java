package me.jiny.boom.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Schema(description = "취향 유형 코드", example = "SCTL")
    private String tasteTypeCode;

    @Schema(description = "취향 유형 이름", example = "효율 추구 스마트 유저")
    private String tasteType;

    @Schema(description = "취향 유형 설명", example = "편안한 실내에서 정보를 수집하고 실속을 챙기는 실용주의자")
    private String tasteTypeDescription;

    @Schema(description = "누적 A 점수 (Activity) - 100점 만점", example = "62")
    private Integer aScore;

    @Schema(description = "누적 P 점수 (Productivity) - 100점 만점", example = "75")
    private Integer pScore;

    @Schema(description = "누적 E 점수 (Emotional) - 100점 만점", example = "81")
    private Integer eScore;

    @Schema(description = "누적 F 점수 (Focus) - 100점 만점", example = "59")
    private Integer fScore;
}

