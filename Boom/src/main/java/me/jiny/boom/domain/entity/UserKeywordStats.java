package me.jiny.boom.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_keyword_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserKeywordStatsId.class)
@Builder
public class UserKeywordStats {

    @Id
    @Column(name = "period", length = 6, nullable = false)
    private String period; // char(6) 형식: YYYYMM

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    @Column(name = "usage_count", nullable = false)
    @Builder.Default
    private Integer usageCount = 0;
}

