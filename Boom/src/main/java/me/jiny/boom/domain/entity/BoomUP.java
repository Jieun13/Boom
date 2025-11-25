package me.jiny.boom.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "boom_ups")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoomUP {

    @EmbeddedId
    private BoomUPId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("cardId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoomUP boomUP = (BoomUP) o;
        return Objects.equals(id, boomUP.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}