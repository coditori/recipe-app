package com.example.demo.service;

import com.example.demo.dto.RecipeDto;
import com.example.demo.dto.RecipeSearchDto;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.model.Recipe;
import com.example.demo.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeDto> searchRecipes(RecipeSearchDto searchDto) {
        List<Recipe> allRecipes = recipeRepository.findAll();

        return allRecipes.stream()
                .filter(recipe -> isVegetarianMatch(recipe, searchDto.getVegetarian()))
                .filter(recipe -> isServingsMatch(recipe, searchDto.getServings()))
                .filter(recipe -> isIncludeIngredientMatch(recipe, searchDto.getIncludeIngredient()))
                .filter(recipe -> isExcludeIngredientMatch(recipe, searchDto.getExcludeIngredient()))
                .filter(recipe -> isInstructionsMatch(recipe, searchDto.getSearchText()))
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());
    }

    public Recipe saveRecipe(RecipeDto recipeDto) {
        validateRecipeDto(recipeDto);

        Recipe recipe = new Recipe();
        recipe.setName(recipeDto.getName());
        recipe.setVegetarian(recipeDto.getIsVegetarian());
        recipe.setServings(recipeDto.getServings());
        recipe.setIngredients(recipeDto.getIngredients());
        recipe.setInstructions(recipeDto.getInstructions());

        return recipeRepository.save(recipe);
    }

    private void validateRecipeDto(RecipeDto recipeDto) {
        if (recipeDto.getName().isEmpty()) {
            throw new InvalidRequestException("Recipe name is required.");
        }

        if (recipeDto.getServings() <= 0) {
            throw new InvalidRequestException("Servings must be a positive value.");
        }

        // Additional validation logic for ingredients, if needed
    }

    private boolean isVegetarianMatch(Recipe recipe, boolean isVegetarian) {
        return !isVegetarian || recipe.isVegetarian();
    }

    private boolean isServingsMatch(Recipe recipe, Integer servings) {
        return servings == null || recipe.getServings() == servings;
    }

    private boolean isIncludeIngredientMatch(Recipe recipe, String includeIngredient) {
        return includeIngredient == null || recipe.getIngredients().contains(includeIngredient);
    }

    private boolean isExcludeIngredientMatch(Recipe recipe, String excludeIngredient) {
        return excludeIngredient == null || !recipe.getIngredients().contains(excludeIngredient);
    }

    private boolean isInstructionsMatch(Recipe recipe, String searchText) {
        return searchText == null || recipe.getInstructions().contains(searchText);
    }
}