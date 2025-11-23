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
@Schema(description = "카드 생성 요청 DTO")
public class CardCreateRequest {

    @NotNull(message = "카테고리 ID는 필수입니다")
    @Schema(description = "카테고리 ID", example = "1", required = true)
    private Long categoryId;

    @Schema(description = "소분류 ID", example = "1")
    private Long subCategoryId;

    @NotBlank(message = "카드 이름은 필수입니다")
    @Size(max = 100, message = "카드 이름은 최대 100자까지 가능합니다")
    @Schema(description = "카드 이름", example = "내 최애 K-POP 앨범", required = true)
    private String name;

    @Size(max = 1000, message = "설명은 최대 1000자까지 가능합니다")
    @Schema(description = "카드 설명", example = "요즘 가장 좋아하는 앨범이에요!")
    private String description;

    @NotNull(message = "Boom Level은 필수입니다")
    @Min(value = 1, message = "Boom Level은 1 이상이어야 합니다")
    @Max(value = 5, message = "Boom Level은 5 이하여야 합니다")
    @Schema(description = "Boom Level (1-5)", example = "5", required = true)
    private Integer boomLevel;

    @NotNull(message = "키워드는 필수입니다")
    @Size(min = 1, max = 3, message = "키워드는 1개 이상 3개 이하여야 합니다")
    @Schema(description = "키워드 ID 리스트 (최대 3개)", example = "[1, 2, 3]", required = true)
    private List<Long> keywordIds;
}

