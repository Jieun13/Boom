package me.jiny.boom.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "월별 통계 응답 DTO")
public class MonthlyStatsResponse {

    @Schema(description = "년월", example = "2024-01")
    private YearMonth yearMonth;

    @Schema(description = "총 카드 수", example = "10")
    private Integer totalCards;

    @Schema(description = "총 BoomUP 수", example = "25")
    private Integer totalBoomUps;
}

