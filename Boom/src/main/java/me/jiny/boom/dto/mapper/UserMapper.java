package me.jiny.boom.dto.mapper;

import me.jiny.boom.domain.entity.User;
import me.jiny.boom.dto.response.UserPublicResponse;
import me.jiny.boom.dto.response.UserResponse;
import me.jiny.boom.dto.response.UserSimpleResponse;

public class UserMapper {

    public static UserSimpleResponse toSimpleResponse(User user) {
        return UserSimpleResponse.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .profileImageUrl(user.getProfileImageUrl())
            .build();
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .nickname(user.getNickname())
            .profileImageUrl(user.getProfileImageUrl())
            .bio(user.getBio())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }

    public static UserPublicResponse toPublicResponse(User user) {
        return UserPublicResponse.builder()
            .nickname(user.getNickname())
            .profileImageUrl(user.getProfileImageUrl())
            .bio(user.getBio())
            .build();
    }
}

