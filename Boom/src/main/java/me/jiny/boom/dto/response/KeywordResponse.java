package me.jiny.boom.dto.response;

import me.jiny.boom.domain.entity.Keyword;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "키워드 정보 응답 DTO")
public class KeywordResponse {

    @Schema(description = "키워드 ID", example = "1")
    private Long id;

    @Schema(description = "키워드 이름", example = "감성적인")
    private String name;

    @Schema(description = "키워드 타입", example = "FEELING")
    private Keyword.KeywordType type;
}

