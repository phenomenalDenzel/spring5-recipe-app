package com.springframework.services;

import com.springframework.commands.RecipeCommand;
import com.springframework.converters.RecipeCommandToRecipe;
import com.springframework.converters.RecipeToRecipeCommand;
import com.springframework.model.Recipe;
import com.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RecipeServiceIT {
    public static final String NEW_DESCRIPTION="Description";
    @Autowired
    RecipeService recipeService;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;
    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;
    @BeforeEach
    void setUp() {
    }

    @Transactional
    @Test
    void saveRecipeCommand() {
        //given
        Iterable<Recipe> recipes=recipeRepository.findAll();
        Recipe recipe=recipes.iterator().next();
        RecipeCommand recipeCommand=recipeToRecipeCommand.convert(recipe);
        recipeCommand.setDescription(NEW_DESCRIPTION);

        //when
        RecipeCommand savedRecipeCommand=recipeService.saveRecipeCommand(recipeCommand);

        //then
        assertEquals(NEW_DESCRIPTION,savedRecipeCommand.getDescription());
        assertEquals(recipe.getId(),recipeCommand.getId());
        assertEquals(recipe.getCategories().size(),recipeCommand.getCategories().size());
        assertEquals(recipe.getIngredients().size(),recipeCommand.getIngredients().size());
        assertNotNull(savedRecipeCommand);
    }
}