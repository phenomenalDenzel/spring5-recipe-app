package com.springframework.services;

import com.springframework.converters.RecipeCommandToRecipe;
import com.springframework.converters.RecipeToRecipeCommand;
import com.springframework.exceptions.NotFoundException;
import com.springframework.model.Recipe;
import com.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RecipeServiceImpTest {
    private RecipeServiceImp recipeServiceImp;
    @Mock
    private RecipeRepository recipeRepository;
    private RecipeCommandToRecipe recipeCommandToRecipe;
    private RecipeToRecipeCommand recipeToRecipeCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        recipeServiceImp=new RecipeServiceImp(recipeRepository,recipeCommandToRecipe,recipeToRecipeCommand);
    }
    @Test
    void getRecipeById(){
        Recipe recipe=new Recipe();
        recipe.setId(1l);
        Optional<Recipe> recipeOptional=Optional.of(recipe);
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        Recipe returnRecipe=recipeServiceImp.findRecipeById(1l);

        assertNotNull(returnRecipe,"Recipe returned null");
        verify(recipeRepository,times(1)).findById(anyLong());
        verify(recipeRepository,never()).findAll();
    }
    @Test
    public void testRecipeNotFound(){
        Optional<Recipe> recipeOptional=Optional.empty();
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        Assertions.assertThrows(NotFoundException.class,()->recipeServiceImp.findRecipeById(1L));

    }
    @Test
    void getRecipes() {
        Set<Recipe> recipeData=new HashSet<>();
        recipeData.add(new Recipe());
        when(recipeServiceImp.getRecipes()).thenReturn(recipeData);
        Set<Recipe> recipes=recipeServiceImp.getRecipes();
        assertEquals(recipes.size(),1);
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void testDeleteById(){
        //given
        Long id=Long.valueOf(21);
        //when
        recipeServiceImp.deleteById(id);

        //then
        verify(recipeRepository).deleteById(anyLong());
    }
}