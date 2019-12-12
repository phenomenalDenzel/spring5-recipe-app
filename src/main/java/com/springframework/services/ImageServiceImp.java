package com.springframework.services;

import com.springframework.model.Recipe;
import com.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Slf4j
public class ImageServiceImp implements ImageService {
    private RecipeRepository recipeRepository;

    public ImageServiceImp(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void saveImageFile(Long id, MultipartFile file){
        try {
            Optional<Recipe> optionalRecipe=recipeRepository.findById(id);
            if(!optionalRecipe.isPresent())
                log.error("Wrong Recipe Id");
            Recipe recipe=optionalRecipe.get();
            Byte[] bytes=new Byte[file.getBytes().length];
            int index=0;
            for(Byte b:file.getBytes()){
                bytes[index++]=b;
            }
            recipe.setImage(bytes);
            recipeRepository.save(recipe);
            log.info("Image uploaded");

        }catch (Exception e){
            log.error("something went wrong");
        }
    }
}
