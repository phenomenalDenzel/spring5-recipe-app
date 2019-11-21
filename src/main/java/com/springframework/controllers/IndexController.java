package com.springframework.controllers;

import com.springframework.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"","/","/index"})
    public String getIndexPage(Model model){
//       Optional<Category> categoryOptional= categoryRepository.findByCategoryName("Mexican");
//        Optional<UnitOfMeasure> unitOfMeasureOptional=unitOfMeasureRepository.findByDescription("Tea Spoon");
//        System.out.println("Cat Id is "+categoryOptional.get().getId());
//        System.out.println("Uom Id is "+unitOfMeasureOptional.get().getId());
        model.addAttribute("recipes",recipeService.getRecipes());
        return "index";
    }
}
