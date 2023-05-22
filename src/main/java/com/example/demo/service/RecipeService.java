package com.example.demo.service;

import com.example.demo.dto.RecipeDto;
import com.example.demo.dto.RecipeSearchDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Ingredient;
import com.example.demo.model.Recipe;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.repository.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final ModelMapper modelMapper;

    public RecipeService(RecipeRepository recipeRepository, ModelMapper modelMapper, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.modelMapper = modelMapper;
        this.ingredientRepository = ingredientRepository;
    }

    public List<RecipeDto> searchRecipes(RecipeSearchDto searchDto) {
        List<Recipe> allRecipes = recipeRepository.findAll();

        if (allRecipes.isEmpty()) {
            throw new NotFoundException("No recipes found.");
        }

        var matchingRecipes = allRecipes.stream()
                .filter(recipe -> isVegetarianMatch(recipe, searchDto.getVegetarian()))
                .filter(recipe -> isServingsMatch(recipe, searchDto.getServings()))
                .filter(recipe -> isIncludeIngredientMatch(recipe, searchDto.getIncludeIngredient()))
                .filter(recipe -> isExcludeIngredientMatch(recipe, searchDto.getExcludeIngredient()))
                .filter(recipe -> isInstructionsMatch(recipe, searchDto.getSearchText()))
                .peek(i -> System.out.println("i = " + i))
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .toList();

        if (matchingRecipes.isEmpty()) {
            throw new NotFoundException("No recipes found matching the search criteria.");
        }

        return matchingRecipes;
    }

    public RecipeDto saveRecipe(RecipeDto recipeDto) {
        Recipe recipe = modelMapper.map(recipeDto, Recipe.class);

        // Save the ingredients first
        Set<Ingredient> savedIngredients = Optional.ofNullable(recipe.getIngredients())
                .orElse(Collections.emptySet())
                .stream()
                .map(ingredientRepository::save)
                .collect(Collectors.toSet());

        // Assign the saved ingredients to the recipe
        recipe.setIngredients(savedIngredients);

        Recipe savedRecipe = recipeRepository.save(recipe);
        return modelMapper.map(savedRecipe, RecipeDto.class);
    }

    private boolean isVegetarianMatch(Recipe recipe, Boolean isVegetarian) {
        if (isVegetarian == null) return true;

        return !isVegetarian || recipe.isVegetarian();
    }

    private boolean isServingsMatch(Recipe recipe, Integer servings) {
        return servings == null || recipe.getServings() == servings;
    }

    private boolean isIncludeIngredientMatch(Recipe recipe, Set<String> ingredients) {
        if (recipe == null || ingredients == null) return true;

        return compareTwoSet(recipe, ingredients);
    }

    private boolean isExcludeIngredientMatch(Recipe recipe, Set<String> ingredients) {
        if (recipe == null || ingredients == null) return true;

        return !compareTwoSet(recipe, ingredients);
    }

    private static boolean compareTwoSet(Recipe recipe, Set<String> ingredients) {
        if (recipe == null || ingredients == null) return true;

        Set<String> recipeIngredients = recipe.getIngredients().stream()
                .map(Ingredient::getName)
                .collect(Collectors.toSet());

        return recipeIngredients.containsAll(ingredients);
    }

    private boolean isInstructionsMatch(Recipe recipe, String searchText) {
        return searchText == null
                || recipe.getInstructions() == null
                || recipe.getInstructions().contains(searchText);
    }
}