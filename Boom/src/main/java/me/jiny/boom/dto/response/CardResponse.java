package me.jiny.boom.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "카드 정보 응답 DTO")
public class CardResponse {

    @Schema(description = "카드 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 정보")
    private UserSimpleResponse user;

    @Schema(description = "카테고리 정보")
    private CategoryResponse category;

    @Schema(description = "소분류 정보")
    private SubCategoryResponse subCategory;

    @Schema(description = "카드 이름", example = "내 최애 K-POP 앨범")
    private String name;

    @Schema(description = "카드 설명", example = "요즘 가장 좋아하는 앨범이에요!")
    private String description;

    @Schema(description = "Boom Level (1-5)", example = "5")
    private Integer boomLevel;

    @Schema(description = "이미지 URL", example = "https://example.com/card.jpg")
    private String imageUrl;

    @Schema(description = "키워드 리스트")
    private List<KeywordResponse> keywords;

    @Schema(description = "BoomUP 수", example = "42")
    private Long boomUpCount;

    @Schema(description = "현재 사용자가 BoomUP 했는지 여부", example = "true")
    private Boolean isBoomUpped;

    @Schema(description = "현재 사용자가 수집했는지 여부", example = "false")
    private Boolean isCollected;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;
}

