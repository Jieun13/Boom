package me.jiny.boom.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "card_keywords")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CardKeyword {

    @EmbeddedId
    private CardKeywordId id;

    @MapsId("cardId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    @JsonIgnoreProperties({"user", "category", "subCategory", "cardKeywords", "boomUps", "collections"})
    private Card card;

    @MapsId("keywordId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    @JsonIgnoreProperties({"cardKeywords"})
    private Keyword keyword;

    // 영속화 후 JPA가 id 세팅하도록 setter만 사용
    public void setCard(Card card) {
        this.card = card;
        if (id == null) id = new CardKeywordId();
        this.id.setCardId(card.getId());
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
        if (id == null) id = new CardKeywordId();
        this.id.setKeywordId(keyword.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardKeyword)) return false;
        CardKeyword that = (CardKeyword) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
