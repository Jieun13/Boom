package me.jiny.boom.domain.repository;

import me.jiny.boom.domain.entity.Card;
import me.jiny.boom.domain.entity.CardCollection;
import me.jiny.boom.domain.entity.CardCollectionId;
import me.jiny.boom.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardCollectionRepository extends JpaRepository<CardCollection, CardCollectionId> {
    boolean existsByUserAndCard(User user, Card card);
    Optional<CardCollection> findByUserAndCard(User user, Card card);
    void deleteByUserAndCard(User user, Card card);
    Page<CardCollection> findByUser(User user, Pageable pageable);
}

