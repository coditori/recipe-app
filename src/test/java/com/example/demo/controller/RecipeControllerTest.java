package com.example.demo.controller;

import com.example.demo.DemoApplication;
import com.example.demo.dto.IngredientDto;
import com.example.demo.dto.RecipeDto;
import com.example.demo.dto.RecipeSearchDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DemoApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RecipeControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateRecipe_Success() {
        RecipeDto recipeDto = getSampleRecipe();

        var response = saveRecipe(recipeDto);
        var savedRecipeDto = response.getBody();

        assertNotNull(savedRecipeDto);
        assertEquals("Lasagna", savedRecipeDto.getName());
        assertFalse(savedRecipeDto.isVegetarian());
        assertEquals(6, savedRecipeDto.getServings());
        assertEquals("1. Cook the ground beef...", savedRecipeDto.getInstructions());

        assertEquals(2, savedRecipeDto.getIngredients().size());
    }

    private static RecipeDto getSampleRecipe() {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("Lasagna");
        recipeDto.setVegetarian(false);
        recipeDto.setServings(6);
        recipeDto.setInstructions("1. Cook the ground beef...");

        IngredientDto ingredientDto1 = new IngredientDto();
        ingredientDto1.setName("Beef");

        IngredientDto ingredientDto2 = new IngredientDto();
        ingredientDto2.setName("Pasta");

        Set<IngredientDto> ingredients = new HashSet<>();
        ingredients.add(ingredientDto1);
        ingredients.add(ingredientDto2);

        recipeDto.setIngredients(ingredients);
        return recipeDto;
    }

    @Test
    public void testCreateRecipe_MissingName() {
        RecipeDto recipeDto = getSampleRecipe();
        recipeDto.setName(null);
        System.out.println("recipeDto = " + recipeDto);

        ResponseEntity<RecipeDto> response = saveRecipe(recipeDto);
        System.out.println("response zzz = " + response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private ResponseEntity<RecipeDto> saveRecipe(RecipeDto recipeDto) {
        ResponseEntity<RecipeDto> recipeDtoResponseEntity = restTemplate.postForEntity("/recipes", recipeDto, RecipeDto.class);
        System.out.println("recipeDtoResponseEntity = " + recipeDtoResponseEntity);
        return recipeDtoResponseEntity;
    }

    private static HttpEntity<RecipeSearchDto> makeRequestEntity(RecipeSearchDto searchDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP entity with the request body and headers
        return new HttpEntity<>(searchDto, headers);
    }

    @Test
    public void testSearchRecipesAllVegetarian() {
        RecipeDto vegetarianRecipe = getSampleRecipe();
        vegetarianRecipe.setName("Vegetarian Recipe");
        vegetarianRecipe.setVegetarian(true);
        saveRecipe(vegetarianRecipe);

        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setVegetarian(true);

        ResponseEntity<List<RecipeDto>> response = restTemplate.exchange(
                "/recipes/search",
                HttpMethod.POST,
                makeRequestEntity(searchDto),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<RecipeDto> recipes = response.getBody();
        Assertions.assertNotNull(recipes, "Recipes should not be null");
        System.out.println("recipes = " + recipes);
        Assertions.assertEquals(1, recipes.size(), "Expected 1 recipe to be returned");
    }

    @Test
    public void testSearchRecipesByServings() throws InterruptedException {
        RecipeDto recipe1 = getSampleRecipe();
        recipe1.setName("Recipe 1");
        recipe1.setServings(4);
        saveRecipe(recipe1);

        RecipeDto recipe2 = getSampleRecipe();
        recipe2.setName("Recipe 2");
        recipe2.setServings(6);
        saveRecipe(recipe2);

        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setServings(4);

        ResponseEntity<List<RecipeDto>> response = restTemplate.exchange(
                "/recipes/search",
                HttpMethod.POST,
                makeRequestEntity(searchDto),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<RecipeDto> recipes = response.getBody();
        System.out.println("recipes = " + recipes);
        Assertions.assertNotNull(recipes, "Recipes should not be null");
        assertEquals(1, recipes.size(), "Expected 1 recipe to be returned");
        assertEquals("Recipe 1", recipes.get(0).getName());
    }

    @Test
    public void testSearchRecipesByIncludeIngredient() {
        RecipeDto recipe1 = getSampleRecipe();
        recipe1.setName("Recipe 1");
        recipe1.setIngredients(Set.of(new IngredientDto("Ingredient 1"), new IngredientDto("Ingredient 2")));
        saveRecipe(recipe1);

        RecipeDto recipe2 = getSampleRecipe();
        recipe2.setName("Recipe 2");
        recipe2.setIngredients(Set.of(new IngredientDto("Ingredient 2"), new IngredientDto("Ingredient 3")));
        saveRecipe(recipe2);

        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setIncludeIngredient(Set.of("Ingredient 2"));

        ResponseEntity<List<RecipeDto>> response = restTemplate.exchange(
                "/recipes/search",
                HttpMethod.POST,
                makeRequestEntity(searchDto),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<RecipeDto> recipes = response.getBody();
        Assertions.assertNotNull(recipes, "Recipes should not be null");
        assertEquals(2, recipes.size(), "Expected 2 recipes to be returned");
        assertEquals("Recipe 1", recipes.get(0).getName());
        assertEquals("Recipe 2", recipes.get(1).getName());
    }

    @Test
    public void testSearchRecipesByExcludeIngredient() {
        RecipeDto recipe1 = getSampleRecipe();
        recipe1.setName("Recipe 1");
        recipe1.setIngredients(Set.of(new IngredientDto("Ingredient 1"), new IngredientDto("Ingredient 2")));
        saveRecipe(recipe1);

        RecipeDto recipe2 = getSampleRecipe();
        recipe2.setName("Recipe 2");
        recipe2.setIngredients(Set.of(new IngredientDto("Ingredient 2"), new IngredientDto("Ingredient 3")));
        saveRecipe(recipe2);

        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setExcludeIngredient(Set.of("Ingredient 2"));

        ResponseEntity<?> response = restTemplate.exchange(
                "/recipes/search",
                HttpMethod.POST,
                makeRequestEntity(searchDto),
                new ParameterizedTypeReference<>() {
                }
        );

        var recipes = response.getBody();
        System.out.println("recipes = " + recipes);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(recipes, "Recipes should not be null");
//        assertEquals(1, recipes.size(), "Expected 1 recipe to be returned");
//        assertEquals("Recipe 2", recipes.get(0).getName());
    }

    @Test
    public void testSearchRecipesBySearchText() {
        RecipeDto recipe1 = getSampleRecipe();
        recipe1.setName("Recipe 1");
        recipe1.setInstructions("Cooking instructions for Recipe 1...");
        saveRecipe(recipe1);

        RecipeDto recipe2 = getSampleRecipe();
        recipe2.setName("Recipe 2");
        recipe2.setInstructions("Cooking instructions for Recipe 2...");

        saveRecipe(recipe2);

        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setSearchText("Recipe 1");

        ResponseEntity<List<RecipeDto>> response = restTemplate.exchange(
                "/recipes/search",
                HttpMethod.POST,
                makeRequestEntity(searchDto),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<RecipeDto> recipes = response.getBody();
        Assertions.assertNotNull(recipes, "Recipes should not be null");
        assertEquals(1, recipes.size(), "Expected 1 recipe to be returned");
        assertEquals("Recipe 1", recipes.get(0).getName());
    }

    @Test
    public void testSearchRecipesMultipleFilters() {
        var recipe1 = getSampleRecipe();
        recipe1.setName("Recipe 1");
        recipe1.setVegetarian(true);
        recipe1.setServings(4);
        recipe1.setIngredients(Set.of(new IngredientDto("Ingredient 1"), new IngredientDto("Ingredient 2")));
        saveRecipe(recipe1);

        var recipe2 = getSampleRecipe();
        recipe2.setName("Recipe 2");
        recipe2.setVegetarian(true);
        recipe2.setServings(6);
        recipe2.setIngredients(Set.of(new IngredientDto("Ingredient 2"), new IngredientDto("Ingredient 3")));
        saveRecipe(recipe2);

        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setVegetarian(true);
        searchDto.setServings(4);
        searchDto.setIncludeIngredient(Set.of("Ingredient 2"));

        ResponseEntity<List<RecipeDto>> response = restTemplate.exchange(
                "/recipes/search",
                HttpMethod.POST,
                makeRequestEntity(searchDto),
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<RecipeDto> recipes = response.getBody();
        Assertions.assertNotNull(recipes, "Recipes should not be null");
        assertEquals(1, recipes.size(), "Expected 1 recipe to be returned");
        assertEquals("Recipe 1", recipes.get(0).getName());
    }

    @Test
    public void testSearchRecipesNoResults() {
        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setVegetarian(true);

        ResponseEntity<?> response = restTemplate.exchange(
                "/recipes/search",
                HttpMethod.POST,
                makeRequestEntity(searchDto),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        var recipes = response.getBody();
        Assertions.assertNull(recipes, "Recipes should be null");
    }


    @Test
    public void testSearchRecipesInvalidInput() {
        RecipeSearchDto searchDto = new RecipeSearchDto();
        searchDto.setServings(-1);

        ResponseEntity<?> response = restTemplate.exchange(
                "/recipes/search",
                HttpMethod.POST,
                makeRequestEntity(searchDto),
                new ParameterizedTypeReference<>() {}
        );

        var recipes = response.getBody();
        System.out.println("recipes = " + recipes);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(recipes, "Recipes should be null");
    }

}