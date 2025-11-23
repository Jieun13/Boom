package me.jiny.boom.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Table(name = "monthly_statistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(MonthlyStatisticsId.class)
@Builder
public class MonthlyStatistics {

    @Id
    @Column(name = "period", length = 6, nullable = false)
    private String period; // char(6) 형식: YYYYMM

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "card_count", nullable = false)
    @Builder.Default
    private Integer cardCount = 0;

    @Column(name = "boom_up_count", nullable = false)
    @Builder.Default
    private Integer boomUpCount = 0;

}

