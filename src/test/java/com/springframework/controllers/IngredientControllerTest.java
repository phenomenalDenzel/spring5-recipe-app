package com.springframework.controllers;

import com.springframework.commands.IngredientCommand;
import com.springframework.commands.RecipeCommand;
import com.springframework.services.IngredientService;
import com.springframework.services.RecipeService;
import com.springframework.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IngredientControllerTest {
    @Mock
    private RecipeService recipeService;
    @Mock
    private IngredientService ingredientService;
    @Mock
    UnitOfMeasureService unitOfMeasureService;
    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        IngredientController controller;
        MockitoAnnotations.initMocks(this);
        controller=new IngredientController(recipeService,ingredientService,unitOfMeasureService);
        mockMvc= MockMvcBuilders.standaloneSetup(controller).build();
    }
    @Test
    void testGetIngredientsView() throws Exception{
        //given
        RecipeCommand recipeCommand=new RecipeCommand();
        recipeCommand.setId(1L);
        //when
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
        //then
        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService).findCommandById(anyLong());
    }

    @Test
    void testShowIngredient() throws Exception{
        IngredientCommand ingredientCommand=new IngredientCommand();
        ingredientCommand.setId(1L);
        when(ingredientService.getIngredientByRecipeId(anyLong(),anyLong())).thenReturn(ingredientCommand);
        mockMvc.perform(get("/recipe/1/ingredient/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));
        verify(ingredientService).getIngredientByRecipeId(anyLong(),anyLong());
    }
    @Test
    void testAddNewIngredientController() throws Exception{
        //then
        RecipeCommand recipeCommand=new RecipeCommand();
        recipeCommand.setId(1l);

        //when
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

        //then
        mockMvc.perform(get("/recipe/1/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));
        verify(recipeService).findCommandById(anyLong());
    }
    @Test
    void testUpdateIngredientView() throws Exception{
        //given
        IngredientCommand command=new IngredientCommand();
        //when
        when(ingredientService.getIngredientByRecipeId(anyLong(),anyLong())).thenReturn(command);
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());
        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"))
                .andExpect(view().name("recipe/ingredient/ingredientform"));
    }

    @Test
    void saveOrUpdate() throws Exception{
        IngredientCommand command=new IngredientCommand();
        command.setRecipeId(2l);
        command.setId(1l);

        when(ingredientService.saveIngredient(any())).thenReturn(command);
        mockMvc.perform(post("/recipe/2/ingredient/")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("id","")
                    .param("description","some string"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/ingredient/1/show"));
    }
    @Test
    void deleteIngredientController()throws Exception{
        mockMvc.perform(get("/recipe/1/ingredient/2/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:recipe/1/ingredients"));
        verify(ingredientService).deleteIngredientById(anyLong(),anyLong());
    }
}