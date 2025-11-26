package me.jiny.boom.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_similarities")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSimilarity {

    @EmbeddedId
    private UserSimilarityId id;

    @MapsId("user1Id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @MapsId("user2Id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    @Column(nullable = false)
    @Setter
    private Double similarityScore;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 편리한 생성자
    public UserSimilarity(User user1, User user2, Double similarityScore) {
        this.id = new UserSimilarityId(user1.getId(), user2.getId());
        this.user1 = user1;
        this.user2 = user2;
        this.similarityScore = similarityScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSimilarity that = (UserSimilarity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
