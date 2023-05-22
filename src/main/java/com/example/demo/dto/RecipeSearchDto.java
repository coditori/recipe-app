package com.example.demo.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeSearchDto {
    private Boolean vegetarian;
    private Integer servings;
    private List<String> includeIngredient;
    private List<String> excludeIngredient;
    private String searchText;
}
