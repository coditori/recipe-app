package com.example.demo.service;

import com.example.demo.dto.RecipeDto;
import com.example.demo.dto.RecipeSearchDto;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.model.Ingredient;
import com.example.demo.model.Recipe;
import com.example.demo.repository.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;

    public RecipeService(RecipeRepository recipeRepository, ModelMapper modelMapper) {
        this.recipeRepository = recipeRepository;
        this.modelMapper = modelMapper;
    }

    public List<RecipeDto> searchRecipes(RecipeSearchDto searchDto) {
        List<Recipe> allRecipes = recipeRepository.findAll();

        return allRecipes.stream()
                .filter(recipe -> isVegetarianMatch(recipe, searchDto.getVegetarian()))
                .filter(recipe -> isServingsMatch(recipe, searchDto.getServings()))
                .filter(recipe -> isIncludeIngredientMatch(recipe, searchDto.getIncludeIngredient()))
                .filter(recipe -> !isIncludeIngredientMatch(recipe, searchDto.getExcludeIngredient()))
                .filter(recipe -> isInstructionsMatch(recipe, searchDto.getSearchText()))
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());
    }

    public RecipeDto saveRecipe(RecipeDto recipeDto) {
        Recipe recipe = modelMapper.map(recipeDto, Recipe.class);
        Recipe savedRecipe = recipeRepository.save(recipe);
        return modelMapper.map(savedRecipe, RecipeDto.class);
    }

    private void validateRecipeDto(RecipeDto recipeDto) {
        if (recipeDto.getName().isEmpty()) {
            throw new InvalidRequestException("Recipe name is required.");
        }

        if (recipeDto.getServings() <= 0) {
            throw new InvalidRequestException("Servings must be a positive value.");
        }
    }

    private boolean isVegetarianMatch(Recipe recipe, boolean isVegetarian) {
        return !isVegetarian || recipe.isVegetarian();
    }

    private boolean isServingsMatch(Recipe recipe, Integer servings) {
        return servings == null || recipe.getServings() == servings;
    }

    private boolean isIncludeIngredientMatch(Recipe recipe, Set<String> includeIngredient) {
        Set<String> recipeIngredients = recipe.getIngredients().stream()
                .map(Ingredient::getName)
                .collect(Collectors.toSet());

        return recipeIngredients.containsAll(includeIngredient);
    }

    private boolean isInstructionsMatch(Recipe recipe, String searchText) {
        return searchText == null || recipe.getInstructions().contains(searchText);
    }
}