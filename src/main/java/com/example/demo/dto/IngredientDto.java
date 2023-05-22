package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class IngredientDto {

    @NotEmpty(message = "Name cannot be empty")
    private String name;
}