package me.jiny.boom.domain.repository;

import me.jiny.boom.domain.entity.User;
import me.jiny.boom.domain.entity.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserScoreRepository extends JpaRepository<UserScore, Long> {
    Optional<UserScore> findByUser(User user);
}

