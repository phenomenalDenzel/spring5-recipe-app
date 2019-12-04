package com.springframework.services;

import com.springframework.commands.RecipeCommand;
import com.springframework.model.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();
    Recipe findRecipeById(long id);
    RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);
}
