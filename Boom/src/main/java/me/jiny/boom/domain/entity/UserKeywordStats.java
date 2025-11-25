package me.jiny.boom.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "user_keyword_stats")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserKeywordStats {

    @EmbeddedId
    private UserKeywordStatsId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("keywordId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    @Column(name = "period", length = 6, nullable = false, insertable = false, updatable = false)
    private String period;

    @Column(name = "usage_count", nullable = false)
    @Builder.Default
    @Setter
    private Integer usageCount = 0;

    // 편한 생성자
    public UserKeywordStats(String period, User user, Keyword keyword, Integer usageCount) {
        this.id = new UserKeywordStatsId(period, user.getId(), keyword.getId());
        this.period = period;
        this.user = user;
        this.keyword = keyword;
        this.usageCount = usageCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserKeywordStats that = (UserKeywordStats) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
