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
@Schema(description = "소분류 정보 응답 DTO")
public class SubCategoryResponse {

    @Schema(description = "소분류 ID", example = "1")
    private Long id;

    @Schema(description = "소분류 이름", example = "K-POP")
    private String name;

    @Schema(description = "소분류 설명", example = "K-POP 음악")
    private String description;
}

