package com.example.demo.controller;

import com.example.demo.dto.RecipeDto;
import com.example.demo.dto.RecipeSearchDto;
import com.example.demo.model.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateRecipe_Success() {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("Lasagna");
        recipeDto.setIsVegetarian(false);
        recipeDto.setServings(6);
        recipeDto.setInstructions("1. Cook the ground beef...");

        ResponseEntity<RecipeDto> response = restTemplate.postForEntity("/recipes", recipeDto, RecipeDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()));
        assertEquals("Lasagna", response.getBody().getName());
        assertEquals(false, response.getBody().getIsVegetarian());
        assertEquals(6, response.getBody().getServings());
        assertEquals("1. Cook the ground beef...", response.getBody().getInstructions());
    }

    @Test
    public void testCreateRecipe_MissingName() {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setIsVegetarian(false);
        recipeDto.setServings(6);
        recipeDto.setInstructions("1. Cook the ground beef...");

        ResponseEntity<Recipe> response = restTemplate.postForEntity("/recipes", recipeDto, Recipe.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    public void testSearchRecipesAllVegetarian() {
        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setVegetarian(true);

        ResponseEntity<?> response = restTemplate.postForEntity("/recipes/search", searchDto, Recipe.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add assertions to check the response body for the expected vegetarian recipes
    }

    @Test
    public void testSearchRecipesByServings() {
        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setServings(4);

        ResponseEntity<?> response = restTemplate.postForEntity("/recipes/search", searchDto, Recipe.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add assertions to check the response body for the expected recipes with 4 servings
    }

    @Test
    public void testSearchRecipesByIncludeIngredient() {
        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setIncludeIngredient(Set.of("potatoes"));

        ResponseEntity<?> response = restTemplate.postForEntity("/recipes/search", searchDto, Recipe.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add assertions to check the response body for the expected recipes with "potatoes" as an ingredient
    }

    @Test
    public void testSearchRecipesByExcludeIngredient() {
        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setExcludeIngredient(Set.of("salmon"));

        ResponseEntity<?> response = restTemplate.postForEntity("/recipes/search", searchDto, Recipe.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add assertions to check the response body for the expected recipes without "salmon" as an ingredient
    }

    @Test
    public void testSearchRecipesBySearchText() {
        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setSearchText("oven");

        ResponseEntity<?> response = restTemplate.postForEntity("/recipes/search", searchDto, Recipe.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add assertions to check the response body for the expected recipes with "oven" in the instructions
    }

    @Test
    public void testSearchRecipesMultipleFilters() {
        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setVegetarian(true);
        searchDto.setServings(4);
        searchDto.setIncludeIngredient(Set.of("potatoes"));

        ResponseEntity<?> response = restTemplate.postForEntity("/recipes/search", searchDto, Recipe.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add assertions to check the response body for the expected recipes with multiple filters applied
    }

    @Test
    public void testSearchRecipesNoResults() {
        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setVegetarian(true);
        searchDto.setIncludeIngredient(Set.of("salmon"));

        ResponseEntity<?> response = restTemplate.postForEntity("/recipes/search", searchDto, Recipe.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add assertions to check the response body for an empty list of recipes
    }

    @Test
    public void testSearchRecipesInvalidInput() {
        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setServings(-1);

        ResponseEntity<?> response = restTemplate.postForEntity("/recipes/search", searchDto, Recipe.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // Add assertions to check the response body for the expected validation error
    }

}