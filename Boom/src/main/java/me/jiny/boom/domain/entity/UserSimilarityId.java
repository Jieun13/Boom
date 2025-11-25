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
public class UserSimilarityId implements Serializable {

    @Column(name = "user1_id")
    private Long user1Id;

    @Column(name = "user2_id")
    private Long user2Id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSimilarityId)) return false;
        UserSimilarityId that = (UserSimilarityId) o;
        return Objects.equals(user1Id, that.user1Id)
                && Objects.equals(user2Id, that.user2Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1Id, user2Id);
    }
}
