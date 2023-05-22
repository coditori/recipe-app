package com.example.demo.controller;

import com.example.demo.dto.RecipeDto;
import com.example.demo.dto.RecipeSearchDto;
import com.example.demo.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(summary = "Create a new recipe")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recipe created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    public ResponseEntity<RecipeDto> createRecipe(@Valid @RequestBody RecipeDto recipeDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.saveRecipe(recipeDto));
    }

    @Operation(summary = "Search recipes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recipes found"),
            @ApiResponse(responseCode = "400", description = "Invalid search request")
    })
    @PostMapping("/search")
    public ResponseEntity<List<RecipeDto>> searchRecipes(@Valid @RequestBody RecipeSearchDto searchDto) {

        return ResponseEntity.ok(recipeService.searchRecipes(searchDto));
    }
}