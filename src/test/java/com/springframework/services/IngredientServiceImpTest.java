package com.springframework.services;

import com.springframework.commands.IngredientCommand;
import com.springframework.commands.UnitOfMeasureCommand;
import com.springframework.converters.IngredientCommandToIngredient;
import com.springframework.converters.IngredientToIngredientCommand;
import com.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.springframework.model.Ingredient;
import com.springframework.model.Recipe;
import com.springframework.model.UnitOfMeasure;
import com.springframework.repositories.IngredientRepository;
import com.springframework.repositories.RecipeRepository;
import com.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IngredientServiceImpTest {
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private  final IngredientCommandToIngredient ingredientCommandToIngredient;
    @Mock
    IngredientRepository ingredientRepository;

    @Mock
    RecipeRepository recipeRepository;
    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    IngredientService ingredientService;

    public IngredientServiceImpTest(){
        ingredientToIngredientCommand=new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        ingredientCommandToIngredient=new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @BeforeEach
    void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        ingredientService=new IngredientServiceImp(recipeRepository,ingredientToIngredientCommand,ingredientCommandToIngredient,ingredientRepository,unitOfMeasureRepository);
    }

    @Test
    void testGetIngredientByRecipeId() throws Exception{
        //given
        Recipe recipe=new Recipe();
        recipe.setId(1L);

        Ingredient ingredient1=new Ingredient();
        ingredient1.setId(1L);
        Ingredient ingredient2=new Ingredient();
        ingredient2.setId(2L);
        Ingredient ingredient3=new Ingredient();
        ingredient3.setId(3L);

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);

        Optional<Recipe> optionalRecipe=Optional.of(recipe);

        //when
        when(recipeRepository.findById(anyLong())).thenReturn(optionalRecipe);
        IngredientCommand ingredientCommand=ingredientService.getIngredientByRecipeId(1L,2L);
        assertEquals(Long.valueOf(1L),ingredientCommand.getRecipeId());
        assertEquals(Long.valueOf(2L),ingredientCommand.getId());
    }
    @Test
    void testUpdateRecipeIngredientCommand(){
        //given
        IngredientCommand command=new IngredientCommand();
        command.setId(1l);
        command.setRecipeId(2l);

        Optional<Recipe> recipeOptional=Optional.of(new Recipe());

        Recipe savedRecipe=new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId(1l);
        //when
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        IngredientCommand savedCommand=ingredientService.saveIngredient(command);

        assertEquals(1l,savedCommand.getId());
        assertNotNull(savedCommand);
        verify(recipeRepository).save(any());
        verify(recipeRepository).findById(anyLong());
    }

    @Test
    void testSaveRecipeIngredientCommand(){
        //given
        IngredientCommand command=new IngredientCommand();
        command.setDescription("some Ingredient");
        command.setAmount(new BigDecimal(2));

        UnitOfMeasureCommand unitOfMeasureCommand=new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(3l);
        command.setUnitOfMeasure(unitOfMeasureCommand);
        command.setRecipeId(2l);

        Optional<Recipe> recipeOptional=Optional.of(new Recipe());

        Recipe savedRecipe=new Recipe();
        UnitOfMeasure unitOfMeasure=new UnitOfMeasure();
        unitOfMeasure.setId(3l);
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setDescription("some Ingredient");
        savedRecipe.getIngredients().iterator().next().setId(1l);
        savedRecipe.getIngredients().iterator().next().setAmount(new BigDecimal(2));
        savedRecipe.getIngredients().iterator().next().setUnitOfMeasure(unitOfMeasure);
        //when
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        IngredientCommand savedCommand=ingredientService.saveIngredient(command);

        assertEquals(1l,savedCommand.getId());
        assertNotNull(savedCommand);
        verify(recipeRepository).save(any());
        verify(recipeRepository).findById(anyLong());
    }
    @Test
    void testDeleteIngredientById(){
        Recipe recipe=new Recipe();
        Ingredient ingredient=new Ingredient();
        ingredient.setId(1l);
        recipe.addIngredient(ingredient);

        Optional<Recipe> recipeOptional=Optional.of(recipe);
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        ingredientService.deleteIngredientById(1l,1l);
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository).save(any(Recipe.class));

    }
}