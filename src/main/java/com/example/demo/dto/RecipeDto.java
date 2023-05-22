package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDto {

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "isVegetarian cannot be null")
    private Boolean isVegetarian;

    @NotNull(message = "Servings cannot be null")
    private Integer servings;

    private Set<IngredientDto> ingredients;

    @NotEmpty(message = "Instructions cannot be empty")
    private String instructions;
}