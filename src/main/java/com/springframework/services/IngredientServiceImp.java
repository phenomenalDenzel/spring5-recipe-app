package com.springframework.services;

import com.springframework.commands.IngredientCommand;
import com.springframework.converters.IngredientCommandToIngredient;
import com.springframework.converters.IngredientToIngredientCommand;
import com.springframework.model.Ingredient;
import com.springframework.model.Recipe;
import com.springframework.repositories.IngredientRepository;
import com.springframework.repositories.RecipeRepository;
import com.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class IngredientServiceImp implements IngredientService {
    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final IngredientRepository ingredientRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImp(RecipeRepository recipeRepository,IngredientToIngredientCommand converter,
                                IngredientCommandToIngredient ingredientCommandToIngredient,IngredientRepository ingredientRepository,
                                UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand =converter;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.ingredientRepository=ingredientRepository;
        this.unitOfMeasureRepository=unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand getIngredientByRecipeId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional=recipeRepository.findById(recipeId);
        if(!recipeOptional.isPresent())
            log.error("Recipe not found of id "+recipeId);
        Recipe recipe=recipeOptional.get();
        Optional<IngredientCommand> optionalIngredientCommand=recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredient-> ingredientToIngredientCommand.convert(ingredient))
                .findFirst();
        if(!optionalIngredientCommand.isPresent())
            log.error("Ingredient with id "+ingredientId+" not found");
        return optionalIngredientCommand.get();
    }

    @Override
    public IngredientCommand saveIngredient(IngredientCommand command) {
        Ingredient detachedIngredient=ingredientCommandToIngredient.convert(command);
        Optional<Recipe> optionalRecipe=recipeRepository.findById(command.getRecipeId());
        if(!optionalRecipe.isPresent()){
            log.error("Recipe not found");
            return new IngredientCommand();
        }else{
            Recipe recipe=optionalRecipe.get();
            Optional<Ingredient> optionalIngredient=recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();
            if(optionalIngredient.isPresent()){
                Ingredient ingredient=optionalIngredient.get();
                ingredient.setAmount(command.getAmount());
                ingredient.setDescription(command.getDescription());
                ingredient.setUnitOfMeasure(unitOfMeasureRepository
                                            .findById(command.getUnitOfMeasure().getId())
                                            .orElseThrow(()->new RuntimeException("Uom not found")));
            }else{
                recipe.addIngredient(detachedIngredient);
            }
            Recipe savedRecipe=recipeRepository.save(recipe);
            Optional<Ingredient> savedIngredient= savedRecipe.getIngredients()
                                                    .stream()
                                                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                                                    .findFirst();
            if(!savedIngredient.isPresent()){
                savedIngredient=savedRecipe.getIngredients().stream()
                        .filter(i->i.getAmount().equals(command.getAmount()))
                        .filter(i->i.getDescription().equals(command.getDescription()))
                        .filter(i->i.getUnitOfMeasure().getId().equals(command.getUnitOfMeasure().getId()))
                        .findFirst();
            }
            return ingredientToIngredientCommand.convert(savedIngredient.get());
        }
    }

    @Override
    public void deleteIngredientById(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional=recipeRepository.findById(recipeId);
        if(!recipeOptional.isPresent())
            log.error("Recipe not found");
        Recipe recipe=recipeOptional.get();
        Optional<Ingredient> ingredientOptional=recipe.getIngredients().stream()
                .filter(i->i.getId().equals(ingredientId))
                .findFirst();
        if(ingredientOptional.isPresent()){
            Ingredient ingredientToDelete=ingredientOptional.get();
            recipe.getIngredients().remove(ingredientToDelete);
            recipeRepository.save(recipe);
            ingredientRepository.deleteById(ingredientToDelete.getId());
        }
    }
}
