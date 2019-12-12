package com.springframework.controllers;

import com.springframework.commands.RecipeCommand;
import com.springframework.exceptions.NotFoundException;
import com.springframework.model.Recipe;
import com.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class RecipeController {
    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}/show")
    public String getRecipe(@PathVariable String id, Model model){
        model.addAttribute("recipe",recipeService.findRecipeById(Long.parseLong(id)));
        return "recipe/show";
    }
    @GetMapping("recipe/new")
    public String newRecipe(Model model){
        model.addAttribute("recipe",new Recipe());
        return "recipe/recipeform";
    }
    @PostMapping("recipe")
    public String saveOrUpdate(@ModelAttribute RecipeCommand recipeCommand){
        RecipeCommand savedRecipeCommand=recipeService.saveRecipeCommand(recipeCommand);
        return "redirect:/recipe/"+savedRecipeCommand.getId()+"/show";
    }
    @GetMapping("recipe/{id}/update")
    public String updateRecipe(@PathVariable String id,Model model){
        model.addAttribute("recipe",recipeService.findCommandById(Long.valueOf(id)));
        return "recipe/recipeform";
    }
    @GetMapping("/recipe/{id}/delete")
    public String deleteRecipe(@PathVariable String id){
        log.debug("Deleting recipe with Id "+id);
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/";
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("404error");
        return modelAndView;
    }
}
