package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeSearchDto {
    private boolean vegetarian;

    @Min(1)
    private Integer servings;

    private String includeIngredient;

    private String excludeIngredient;

    private String searchText;

}
