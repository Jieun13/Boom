package me.jiny.boom.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sub_categories")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @Setter
    private Category category;

    @Column(nullable = false, length = 50)
    @Setter
    private String name;

    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Card> cards = new ArrayList<>();
}

