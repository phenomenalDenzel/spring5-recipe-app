package com.springframework.controllers;

import com.springframework.commands.RecipeCommand;
import com.springframework.services.ImageService;
import com.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ImageControllerTest {
    MockMvc mockMvc;
    @Mock
    ImageService imageService;
    @Mock
    RecipeService recipeService;
    ImageController controller;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller=new ImageController(imageService,recipeService);
        mockMvc= MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void getImageTest() throws Exception{
        //given
        RecipeCommand recipeCommand=new RecipeCommand();
        recipeCommand.setId(1l);

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
        //when
        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(model().attributeExists("recipe"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/imageuploadform"));
        verify(recipeService).findCommandById(anyLong());
    }

    @Test
    void handleImagePost() throws Exception{
        MockMultipartFile mockMultipartFile=
                new MockMultipartFile("imagefile","testing.txt","text/plain","Spring Framework Guru".getBytes());
        mockMvc.perform(multipart("/recipe/1/image").file(mockMultipartFile))
                .andExpect(status().isFound())
                .andExpect(header().string("Location","/recipe/1/show"));
        verify(imageService).saveImageFile(anyLong(),any());
    }
    @Test
    void testRenderImageFromDB() throws Exception{
        RecipeCommand recipeCommand=new RecipeCommand();
        recipeCommand.setId(1l);

        String s="fake image text";
        Byte[] byteBoxed=new Byte[s.getBytes().length];
        int x=0;
        for(byte primByte:s.getBytes()){
            byteBoxed[x++]=primByte;
        }
        recipeCommand.setImage(byteBoxed);

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        MockHttpServletResponse response=mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] byteResponse=response.getContentAsByteArray();
        assertEquals(s.getBytes().length,byteResponse.length);
    }

    @Test
    void handleNumberFormatException() throws Exception{
        mockMvc.perform(get("/recipe/dd/recipeimage"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }
}