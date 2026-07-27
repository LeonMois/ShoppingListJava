package shoppinglist.backend.controller;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shoppinglist.backend.dto.RecipeDto;
import shoppinglist.backend.dto.ShoppingListDto;
import shoppinglist.backend.service.ShoppingListService;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShoppingListController.class)
@DisplayName("ShoppingListController Tests")
class ShoppingListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ShoppingListService shoppingListService;

    private ShoppingListDto sampleDto() {
        return new ShoppingListDto("Apple", "kg", 2f, false, "Fruits");
    }

    @Test
    @DisplayName("GET /shopping-list should return all shopping list items")
    void testGetShoppingList() throws Exception {
        when(shoppingListService.getAll(null)).thenReturn(List.of(sampleDto()));

        mockMvc.perform(get("/shopping-list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("Apple"));

        verify(shoppingListService, times(1)).getAll(null);
    }

    @Test
    @DisplayName("GET /shopping-list with sortOrder should pass it through")
    void testGetShoppingList_WithSortOrder() throws Exception {
        when(shoppingListService.getAll("asc")).thenReturn(List.of(sampleDto()));

        mockMvc.perform(get("/shopping-list").param("sortOrder", "asc"))
                .andExpect(status().isOk());

        verify(shoppingListService, times(1)).getAll("asc");
    }

    @Test
    @DisplayName("POST /shopping-list/add/items should add items")
    void testAddItemToShoppingList() throws Exception {
        ShoppingListDto dto = sampleDto();
        when(shoppingListService.addItems(anyList())).thenReturn(List.of(dto));

        mockMvc.perform(post("/shopping-list/add/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(List.of(dto))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("Apple"));

        verify(shoppingListService, times(1)).addItems(anyList());
    }

    @Test
    @DisplayName("PUT /shopping-list/toggle/items should toggle items")
    void testSetItemsToDeleted() throws Exception {
        ShoppingListDto dto = sampleDto();
        dto.setDeleted(true);
        when(shoppingListService.setItemsToDeleted(anyList())).thenReturn(List.of(dto));

        mockMvc.perform(put("/shopping-list/toggle/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(List.of(dto))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].deleted").value(true));

        verify(shoppingListService, times(1)).setItemsToDeleted(anyList());
    }

    @Test
    @DisplayName("DELETE /shopping-list/delete should remove deleted items")
    void testRemoveItems() throws Exception {
        when(shoppingListService.removeItems()).thenReturn(List.of());

        mockMvc.perform(delete("/shopping-list/delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(shoppingListService, times(1)).removeItems();
    }

    @Test
    @DisplayName("POST /shopping-list/add/recipes should add recipes to the shopping list")
    void testAddRecipeToShoppingList() throws Exception {
        RecipeDto recipe = new RecipeDto("Pasta", 2);
        ShoppingListDto dto = sampleDto();
        when(shoppingListService.addRecipes(anyList())).thenReturn(List.of(dto));

        mockMvc.perform(post("/shopping-list/add/recipes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(List.of(recipe))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("Apple"));

        verify(shoppingListService, times(1)).addRecipes(anyList());
    }

    @Test
    @DisplayName("POST /shopping-list/add/items should propagate IOException for missing item")
    void testAddItemToShoppingList_ThrowsIOException() throws Exception {
        ShoppingListDto dto = sampleDto();
        when(shoppingListService.addItems(anyList())).thenThrow(new IOException("Item not found"));

        assertThrows(IOException.class, () -> mockMvc.perform(post("/shopping-list/add/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(List.of(dto)))));
    }
}
