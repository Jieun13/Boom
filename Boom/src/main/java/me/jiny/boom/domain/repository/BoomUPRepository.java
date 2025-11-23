package me.jiny.boom.domain.repository;

import me.jiny.boom.domain.entity.BoomUP;
import me.jiny.boom.domain.entity.BoomUPId;
import me.jiny.boom.domain.entity.Card;
import me.jiny.boom.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoomUPRepository extends JpaRepository<BoomUP, BoomUPId> {
    boolean existsByUserAndCard(User user, Card card);
    Optional<BoomUP> findByUserAndCard(User user, Card card);
    void deleteByUserAndCard(User user, Card card);
}

