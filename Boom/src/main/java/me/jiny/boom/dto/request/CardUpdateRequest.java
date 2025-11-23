package me.jiny.boom.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "카드 수정 요청 DTO")
public class CardUpdateRequest {

    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;

    @Schema(description = "소분류 ID", example = "1")
    private Long subCategoryId;

    @Size(max = 100, message = "카드 이름은 최대 100자까지 가능합니다")
    @Schema(description = "카드 이름", example = "내 최애 K-POP 앨범")
    private String name;

    @Size(max = 1000, message = "설명은 최대 1000자까지 가능합니다")
    @Schema(description = "카드 설명", example = "요즘 가장 좋아하는 앨범이에요!")
    private String description;

    @Min(value = 1, message = "Boom Level은 1 이상이어야 합니다")
    @Max(value = 5, message = "Boom Level은 5 이하여야 합니다")
    @Schema(description = "Boom Level (1-5)", example = "5")
    private Integer boomLevel;

    @Size(max = 3, message = "키워드는 최대 3개까지 가능합니다")
    @Schema(description = "키워드 ID 리스트 (최대 3개)", example = "[1, 2, 3]")
    private List<Long> keywordIds;
}

