package me.jiny.boom.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Setter
    private String name;

    @Column(name = "a_score")
    @Setter
    private Integer aScore;

    @Column(name = "p_score")
    @Setter
    private Integer pScore;

    @Column(name = "e_score")
    @Setter
    private Integer eScore;

    @Column(name = "f_score")
    @Setter
    private Integer fScore;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Card> cards = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SubCategory> subCategories = new ArrayList<>();
}

