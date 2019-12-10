package com.springframework.services;


import com.springframework.commands.IngredientCommand;

public interface IngredientService {
    public IngredientCommand getIngredientByRecipeId(Long recipeId, Long ingredientId);
    public IngredientCommand saveIngredient(IngredientCommand command);
    public void deleteIngredientById(Long recipeId,Long ingredientId);
}
