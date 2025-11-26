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
@Schema(description = "다른 사용자 프로필 정보 응답 DTO")
public class UserPublicResponse {

    @Schema(description = "닉네임", example = "사용자")
    private String nickname;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/image.jpg")
    private String profileImageUrl;

    @Schema(description = "자기소개", example = "안녕하세요! 개발을 좋아하는 사용자입니다.")
    private String bio;
}

