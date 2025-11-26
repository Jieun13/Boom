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
public class MonthlyStatisticsId implements Serializable {

    @Column(name = "period", length = 6)
    private String period;

    @Column(name = "user_id")
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MonthlyStatisticsId)) return false;
        MonthlyStatisticsId that = (MonthlyStatisticsId) o;
        return Objects.equals(period, that.period)
                && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(period, userId);
    }
}
