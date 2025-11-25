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
public class UserKeywordStatsId implements Serializable {

    @Column(name = "period")
    private String period;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "keyword_id")
    private Long keywordId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserKeywordStatsId)) return false;
        UserKeywordStatsId that = (UserKeywordStatsId) o;
        return Objects.equals(period, that.period)
                && Objects.equals(userId, that.userId)
                && Objects.equals(keywordId, that.keywordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(period, userId, keywordId);
    }
}
