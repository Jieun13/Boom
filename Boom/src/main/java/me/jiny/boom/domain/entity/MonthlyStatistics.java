package me.jiny.boom.domain.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "monthly_statistics")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyStatistics {

    @EmbeddedId
    private MonthlyStatisticsId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "period", length = 6, nullable = false, insertable = false, updatable = false)
    private String period; // YYYYMM

    @Column(name = "card_count", nullable = false)
    @Builder.Default
    @Setter
    private Integer cardCount = 0;

    @Column(name = "boom_up_count", nullable = false)
    @Builder.Default
    @Setter
    private Integer boomUpCount = 0;

    // 편한 생성자
    public MonthlyStatistics(String period, User user, Integer cardCount, Integer boomUpCount) {
        this.id = new MonthlyStatisticsId(period, user.getId());
        this.period = period;
        this.user = user;
        this.cardCount = cardCount != null ? cardCount : 0;
        this.boomUpCount = boomUpCount != null ? boomUpCount : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyStatistics that = (MonthlyStatistics) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
