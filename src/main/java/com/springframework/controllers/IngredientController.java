package com.springframework.controllers;

import com.springframework.commands.IngredientCommand;
import com.springframework.commands.RecipeCommand;
import com.springframework.commands.UnitOfMeasureCommand;
import com.springframework.services.IngredientService;
import com.springframework.services.RecipeService;
import com.springframework.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class IngredientController {
    private RecipeService recipeService;
    private IngredientService ingredientService;
    private UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService=ingredientService;
        this.unitOfMeasureService=unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("recipe/{id}/ingredients")
    public String getIngredientsForRecipe(@PathVariable Long id, Model model){
        log.debug("Getting ingredients for recipe");
        model.addAttribute("recipe",recipeService.findCommandById(id));
        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String getIngredientForRecipe(@PathVariable Long recipeId,@PathVariable Long ingredientId,
                                         Model model){
        model.addAttribute("ingredient",ingredientService.getIngredientByRecipeId(recipeId,ingredientId));
        return "recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/new")
    public String newRecipeIngredient(@PathVariable Long recipeId,Model model){
        RecipeCommand recipeCommand=recipeService.findCommandById(recipeId);
        if(recipeCommand==null)
            throw new RuntimeException("Wrong recipe id");
        IngredientCommand ingredientCommand=new IngredientCommand();
        ingredientCommand.setRecipeId(recipeCommand.getId());
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());
        model.addAttribute("ingredient",ingredientCommand);
        model.addAttribute("uomList",unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientform";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateIngredient(@PathVariable Long recipeId,@PathVariable Long ingredientId,
                                   Model model){
        model.addAttribute("ingredient",ingredientService.getIngredientByRecipeId(recipeId,ingredientId));
        model.addAttribute("uomList",unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientform";
    }

    @PostMapping
    @RequestMapping("recipe/{recipeId}/ingredient")
    public String saveIngredient(@ModelAttribute IngredientCommand command){
        IngredientCommand savedCommand=ingredientService.saveIngredient(command);
        return "redirect:/recipe/"+savedCommand.getRecipeId()+"/ingredient/"+savedCommand.getId()+"/show";
    }
    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable Long recipeId,@PathVariable Long ingredientId){
        ingredientService.deleteIngredientById(recipeId,ingredientId);
        return "redirect:/recipe/"+recipeId+"/ingredients";
    }
}
