package com.example.demo.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RecipeSearchDto {
    private Boolean vegetarian;
    private Integer servings;
    private Set<String> includeIngredient;
    private Set<String> excludeIngredient;
    private String searchText;
}
