package shoppinglist.backend.controller;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shoppinglist.backend.dto.RecipeItemDto;
import shoppinglist.backend.entity.CategoryEntity;
import shoppinglist.backend.entity.ItemEntity;
import shoppinglist.backend.entity.RecipeEntity;
import shoppinglist.backend.entity.RecipeItemEntity;
import shoppinglist.backend.entity.UnitEntity;
import shoppinglist.backend.service.RecipeItemService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeItemController.class)
@DisplayName("RecipeItemController Tests")
class RecipeItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecipeItemService recipeItemService;

    private RecipeItemDto sampleDto() {
        return new RecipeItemDto("Pasta", "Tomato", "kg", "Vegetables", 2f);
    }

    private RecipeItemEntity sampleEntity() {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setRecipeName("Pasta");
        recipe.setServings(2);

        ItemEntity item = new ItemEntity();
        item.setItemName("Tomato");

        UnitEntity unit = new UnitEntity();
        unit.setUnitName("kg");
        item.setUnit(unit);

        CategoryEntity category = new CategoryEntity();
        category.setCategoryName("Vegetables");
        item.setCategory(category);

        RecipeItemEntity entity = new RecipeItemEntity();
        entity.setRecipe(recipe);
        entity.setItem(item);
        entity.setQuantity(2f);
        return entity;
    }

    @Test
    @DisplayName("GET /recipe-items should return all recipe items")
    void testGetRecipeItems() throws Exception {
        when(recipeItemService.getAllRecipeItems()).thenReturn(List.of(sampleDto()));

        mockMvc.perform(get("/recipe-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipeName").value("Pasta"));

        verify(recipeItemService, times(1)).getAllRecipeItems();
    }

    @Test
    @DisplayName("GET /recipe-items/{recipeName} should return items for a recipe")
    void testGetRecipeItemsForRecipe() throws Exception {
        when(recipeItemService.getItemsForRecipe("Pasta")).thenReturn(List.of(sampleEntity()));

        mockMvc.perform(get("/recipe-items/Pasta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipeName").value("Pasta"));

        verify(recipeItemService, times(1)).getItemsForRecipe("Pasta");
    }

    @Test
    @DisplayName("GET /recipe-items/{recipeName} should propagate IOException for missing recipe")
    void testGetRecipeItemsForRecipe_NotFound() throws Exception {
        when(recipeItemService.getItemsForRecipe("Pasta")).thenThrow(new IOException("Recipe not found"));

        assertThrows(IOException.class, () -> mockMvc.perform(get("/recipe-items/Pasta")));
    }

    @Test
    @DisplayName("POST /recipe-items/add should add items to a recipe")
    void testAddItemsToRecipe() throws Exception {
        RecipeItemDto dto = sampleDto();
        when(recipeItemService.addItemsToRecipe(anyList())).thenReturn(List.of(dto));

        mockMvc.perform(post("/recipe-items/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(List.of(dto))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("Tomato"));

        verify(recipeItemService, times(1)).addItemsToRecipe(anyList());
    }

    @Test
    @DisplayName("DELETE /recipe-items/delete should delete recipe items")
    void testDeleteRecipeItem() throws Exception {
        RecipeItemDto dto = sampleDto();
        when(recipeItemService.deleteRecipeItem(anyList())).thenReturn(List.of(dto));

        mockMvc.perform(delete("/recipe-items/delete")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(List.of(dto))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("Tomato"));

        verify(recipeItemService, times(1)).deleteRecipeItem(anyList());
    }

    @Test
    @DisplayName("PUT /recipe-items/update should update recipe items")
    void testUpdateRecipeItem() throws Exception {
        RecipeItemDto oldDto = sampleDto();
        RecipeItemDto newDto = new RecipeItemDto("Pasta", "Tomato", "kg", "Vegetables", 5f);
        when(recipeItemService.updateRecipeItem(anyList())).thenReturn(List.of(newDto));

        mockMvc.perform(put("/recipe-items/update")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Arrays.asList(oldDto, newDto))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(5.0));

        verify(recipeItemService, times(1)).updateRecipeItem(anyList());
    }
}
