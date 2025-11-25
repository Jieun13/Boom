package me.jiny.boom.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoomUPId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "card_id")
    private Long cardId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoomUPId)) return false;
        BoomUPId that = (BoomUPId) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(cardId, that.cardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, cardId);
    }
}
