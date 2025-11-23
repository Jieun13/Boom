package me.jiny.boom.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 프로필 수정 요청 DTO")
public class UserProfileUpdateRequest {

    @Size(max = 50, message = "닉네임은 최대 50자까지 가능합니다")
    @Schema(description = "닉네임", example = "사용자")
    private String nickname;

    @Size(max = 500, message = "프로필 이미지 URL은 최대 500자까지 가능합니다")
    @Schema(description = "프로필 이미지 URL", example = "https://example.com/image.jpg")
    private String profileImageUrl;

    @Size(max = 500, message = "자기소개는 최대 500자까지 가능합니다")
    @Schema(description = "자기소개", example = "안녕하세요! 개발을 좋아하는 사용자입니다.")
    private String bio;
}