package com.springframework.controllers;

import com.springframework.commands.RecipeCommand;
import com.springframework.exceptions.NotFoundException;
import com.springframework.model.Recipe;
import com.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecipeControllerTest {
    @Mock
    RecipeService recipeService;
    RecipeController recipeController;
    MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
       recipeController= new RecipeController(recipeService);
       mockMvc= MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    void getRecipe() throws Exception{
        Recipe recipe=new Recipe();
        recipe.setId(1l);
        when(recipeService.findRecipeById(anyLong())).thenReturn(recipe);
        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }
    @Test
    void getRecipeNotFound() throws Exception{
        Recipe recipe=new Recipe();
        recipe.setId(1l);

        when(recipeService.findRecipeById(anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound());
    }

    @Test
    void tesGetNewRecipeForm() throws Exception{
        RecipeCommand recipeCommand=new RecipeCommand();
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void testPostNewRecipe() throws Exception{
        RecipeCommand recipeCommand=new RecipeCommand();
        recipeCommand.setId(2l);

        when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCommand);

        mockMvc.perform(post("/recipe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id","")
                        .param("description","some string"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/show"));

    }

    @Test
    void testUpdateRecipe() throws Exception{
        RecipeCommand recipeCommand=new RecipeCommand();
        recipeCommand.setId(2l);
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
        mockMvc.perform(get("/recipe/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }
    @Test
    void testDeleteRecipeAction()throws Exception{
        mockMvc.perform(get("/recipe/2/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
        verify(recipeService).deleteById(anyLong());
    }
}