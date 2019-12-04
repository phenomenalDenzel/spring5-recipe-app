package com.springframework.controllers;

import com.springframework.commands.RecipeCommand;
import com.springframework.model.Recipe;
import com.springframework.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RecipeController {
    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }
    @RequestMapping("/recipe/show/{id}")
    public String getRecipe(@PathVariable String id, Model model){
        model.addAttribute("recipe",recipeService.findRecipeById(Long.parseLong(id)));
        return "recipe/show";
    }
    @RequestMapping("recipe/new")
    public String newRecipe(Model model){
        model.addAttribute("recipe",new Recipe());
        return "/recipe/recipeform";
    }
    @PostMapping
    @RequestMapping("recipe")
    public String saveOrUpdate(@ModelAttribute RecipeCommand recipeCommand){
        RecipeCommand savedRecipeCommand=recipeService.saveRecipeCommand(recipeCommand);
        return "redirect:/recipe/show/"+savedRecipeCommand.getId();
    }
}
