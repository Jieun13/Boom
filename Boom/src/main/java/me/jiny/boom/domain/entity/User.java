package me.jiny.boom.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    @Setter
    private String password;

    @Column(length = 50)
    @Setter
    private String nickname;

    @Column(length = 500)
    @Setter
    private String profileImageUrl;

    @Column(length = 500)
    @Setter
    private String bio;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Card> cards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoomUP> boomUps = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CardCollection> collections = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserScore> scores = new ArrayList<>();

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserSimilarity> similaritiesAsUser1 = new ArrayList<>();

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserSimilarity> similaritiesAsUser2 = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserKeywordStats> keywordStats = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MonthlyStatistics> monthlyStatistics = new ArrayList<>();
}

