package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RecipeDto {

    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    private boolean isVegetarian;

    @NotNull(message = "Servings cannot be null")
    @Min(value = 0, message = "Servings cannot be less than 0")
    private Integer servings;

    @NotNull(message = "Ingredients cannot be null")
    @NotEmpty(message = "Ingredients cannot be empty")
    private Set<IngredientDto> ingredients;

    @NotNull(message = "Instructions cannot be null")
    @NotEmpty(message = "Instructions cannot be empty")
    private String instructions;
}