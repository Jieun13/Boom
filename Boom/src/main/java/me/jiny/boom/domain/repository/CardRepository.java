package me.jiny.boom.domain.repository;

import me.jiny.boom.domain.entity.Card;
import me.jiny.boom.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    Page<Card> findByUser(User user, Pageable pageable);
    Page<Card> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Card> findAllByOrderByBoomUpCountDesc(Pageable pageable);
}

