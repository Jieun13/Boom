package me.jiny.boom.domain.repository;

import me.jiny.boom.domain.entity.Card;
import me.jiny.boom.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    
    @Query("SELECT DISTINCT c FROM Card c " +
           "LEFT JOIN FETCH c.user " +
           "LEFT JOIN FETCH c.category " +
           "LEFT JOIN FETCH c.subCategory " +
           "LEFT JOIN FETCH c.cardKeywords ck " +
           "LEFT JOIN FETCH ck.keyword " +
           "LEFT JOIN FETCH c.boomUps " +
           "LEFT JOIN FETCH c.collections " +
           "WHERE c.user = :user")
    List<Card> findByUser(@Param("user") User user);
    
    @Query("SELECT DISTINCT c FROM Card c " +
           "LEFT JOIN FETCH c.user " +
           "LEFT JOIN FETCH c.category " +
           "LEFT JOIN FETCH c.subCategory " +
           "LEFT JOIN FETCH c.cardKeywords ck " +
           "LEFT JOIN FETCH ck.keyword " +
           "LEFT JOIN FETCH c.boomUps " +
           "LEFT JOIN FETCH c.collections " +
           "ORDER BY c.createdAt DESC")
    List<Card> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT DISTINCT c FROM Card c " +
           "LEFT JOIN FETCH c.user " +
           "LEFT JOIN FETCH c.category " +
           "LEFT JOIN FETCH c.subCategory " +
           "LEFT JOIN FETCH c.cardKeywords ck " +
           "LEFT JOIN FETCH ck.keyword " +
           "LEFT JOIN FETCH c.boomUps " +
           "LEFT JOIN FETCH c.collections " +
           "ORDER BY c.boomUpCount DESC")
    List<Card> findAllByOrderByBoomUpCountDesc();
    
    @Query("SELECT DISTINCT c FROM Card c " +
           "LEFT JOIN FETCH c.user " +
           "LEFT JOIN FETCH c.category " +
           "LEFT JOIN FETCH c.subCategory " +
           "LEFT JOIN FETCH c.cardKeywords ck " +
           "LEFT JOIN FETCH ck.keyword " +
           "LEFT JOIN FETCH c.boomUps " +
           "LEFT JOIN FETCH c.collections " +
           "WHERE c.id = :cardId")
    Optional<Card> findByIdWithAll(@Param("cardId") Long cardId);
}

