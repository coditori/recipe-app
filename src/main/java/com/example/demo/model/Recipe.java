package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private boolean isVegetarian;

    private int servings;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Ingredient> ingredients;

    @Lob
    private String instructions;
}
