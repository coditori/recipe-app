package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class IngredientDto {

    @NotEmpty(message = "Name cannot be empty")
    private String name;
}