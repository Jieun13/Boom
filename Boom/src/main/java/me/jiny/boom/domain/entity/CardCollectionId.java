package me.jiny.boom.domain.entity;

import lombok.*;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardCollectionId implements Serializable {
    private Long userId;
    private Long cardId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardCollectionId)) return false;
        CardCollectionId that = (CardCollectionId) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(cardId, that.cardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, cardId);
    }
}
