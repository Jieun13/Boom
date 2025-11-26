package me.jiny.boom.domain.repository;

import me.jiny.boom.domain.entity.User;
import me.jiny.boom.domain.entity.UserSimilarity;
import me.jiny.boom.domain.entity.UserSimilarityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSimilarityRepository extends JpaRepository<UserSimilarity, UserSimilarityId> {
    Optional<UserSimilarity> findByUser1AndUser2(User user1, User user2);
}

