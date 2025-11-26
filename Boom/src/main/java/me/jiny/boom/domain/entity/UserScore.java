package me.jiny.boom.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_scores")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Setter
    private User user;

    @Column(name = "activity_score")
    @Setter
    private Integer activityScore;

    @Column(name = "productivity_score")
    @Setter
    private Integer productivityScore;

    @Column(name = "emotional_score")
    @Setter
    private Integer emotionalScore;

    @Column(name = "focus_score")
    @Setter
    private Integer focusScore;

    @Column(name = "type_name", length = 100)
    @Setter
    private String typeName;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

