package me.jiny.boom.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "keywords")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private KeywordType type;

    @Column(name = "usage_count")
    @Builder.Default
    @Setter
    private Integer usageCount = 0;

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CardKeyword> cardKeywords = new ArrayList<>();

    public enum KeywordType {
        FEELING,      // 느낌 키워드
        ACTION,       // 행동 키워드
        TENDENCY      // 성향 키워드
    }
}

