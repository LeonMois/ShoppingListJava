package shoppinglist.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shoppinglist.backend.dto.RecipeDto;
import shoppinglist.backend.entity.RecipeEntity;
import shoppinglist.backend.service.RecipeService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
@DisplayName("RecipeController Tests")
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecipeService recipeService;

    @Test
    @DisplayName("GET /recipes should return all recipes")
    void testGetRecipes() throws Exception {
        when(recipeService.getAllRecipes()).thenReturn(List.of(new RecipeDto("Pasta", 2)));

        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pasta"));

        verify(recipeService, times(1)).getAllRecipes();
    }

    @Test
    @DisplayName("GET /recipes/{recipe} should return one recipe")
    void testGetOneRecipe() throws Exception {
        RecipeEntity entity = new RecipeEntity();
        entity.setRecipeName("Pasta");
        entity.setServings(2);
        when(recipeService.getSingleRecipe("Pasta")).thenReturn(entity);

        mockMvc.perform(get("/recipes/Pasta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pasta"));

        verify(recipeService, times(1)).getSingleRecipe("Pasta");
    }

    @Test
    @DisplayName("GET /recipes/{recipe} should propagate IOException for missing recipe")
    void testGetOneRecipe_NotFound() throws Exception {
        when(recipeService.getSingleRecipe("Pasta")).thenThrow(new IOException("Recipe not found"));

        assertThrows(IOException.class, () -> mockMvc.perform(get("/recipes/Pasta")));
    }

    @Test
    @DisplayName("POST /recipes/add should add a recipe")
    void testAddRecipe() throws Exception {
        RecipeDto recipe = new RecipeDto("Pasta", 2);
        when(recipeService.addRecipe(any(RecipeDto.class))).thenReturn(recipe);

        mockMvc.perform(post("/recipes/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pasta"));

        verify(recipeService, times(1)).addRecipe(any(RecipeDto.class));
    }

    @Test
    @DisplayName("DELETE /recipes/delete should delete a recipe")
    void testDeleteRecipe() throws Exception {
        RecipeDto recipe = new RecipeDto("Pasta", 2);
        when(recipeService.deleteRecipe(any(RecipeDto.class))).thenReturn(recipe);

        mockMvc.perform(delete("/recipes/delete")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pasta"));

        verify(recipeService, times(1)).deleteRecipe(any(RecipeDto.class));
    }

    @Test
    @DisplayName("PUT /recipes/update should update a recipe")
    void testUpdateRecipe() throws Exception {
        RecipeDto oldRecipe = new RecipeDto("Pasta", 2);
        RecipeDto newRecipe = new RecipeDto("Pasta", 4);
        when(recipeService.updateRecipe(any(RecipeDto.class), any(RecipeDto.class))).thenReturn(newRecipe);

        mockMvc.perform(put("/recipes/update")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Arrays.asList(oldRecipe, newRecipe))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.servings").value(4));

        verify(recipeService, times(1)).updateRecipe(any(RecipeDto.class), any(RecipeDto.class));
    }
}
