package me.jiny.boom.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardKeywordId implements Serializable {

    @Column(name = "card_id")
    private Long cardId;

    @Column(name = "keyword_id")
    private Long keywordId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardKeywordId)) return false;
        CardKeywordId that = (CardKeywordId) o;
        return Objects.equals(cardId, that.cardId) &&
                Objects.equals(keywordId, that.keywordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, keywordId);
    }
}
