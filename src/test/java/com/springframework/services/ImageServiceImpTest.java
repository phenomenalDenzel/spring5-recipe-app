package com.springframework.services;

import com.springframework.model.Recipe;
import com.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ImageServiceImpTest {
    @Mock
    RecipeRepository recipeRepository;
    ImageService imageService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        imageService=new ImageServiceImp(recipeRepository);
    }

    @Test
    void saveImageFile() throws Exception{
        //given
        MultipartFile multipartFile=
                new MockMultipartFile("imagefile","testing.txt","text/plain","Spring FrameWork Guru".getBytes());
        Recipe recipe=new Recipe();
        recipe.setId(1l);
        Optional<Recipe> recipeOptional=Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        ArgumentCaptor<Recipe> argumentCaptor=ArgumentCaptor.forClass(Recipe.class);
        //when
        imageService.saveImageFile(anyLong(),multipartFile);
        verify(recipeRepository).save(argumentCaptor.capture());
        Recipe savedRecipe=argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length,savedRecipe.getImage().length);
    }
}