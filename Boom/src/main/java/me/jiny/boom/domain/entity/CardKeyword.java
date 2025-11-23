package me.jiny.boom.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "card_keywords")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(CardKeywordId.class)
public class CardKeyword {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;
}

