package shoppinglist.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shoppinglist.backend.dto.RecipeItemDto;
import shoppinglist.backend.entity.CategoryEntity;
import shoppinglist.backend.entity.ItemEntity;
import shoppinglist.backend.entity.RecipeEntity;
import shoppinglist.backend.entity.RecipeItemEntity;
import shoppinglist.backend.repository.RecipeItemRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("RecipeItemService Tests")
class RecipeItemServiceTest {

    @Mock
    private RecipeItemRepository recipeItemRepository;

    @Mock
    private RecipeService recipeService;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private RecipeItemService recipeItemService;

    private RecipeEntity testRecipe;
    private ItemEntity testItem;
    private RecipeItemEntity testRecipeItem;
    private RecipeItemDto testRecipeItemDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testRecipe = new RecipeEntity();
        testRecipe.setId(1);
        testRecipe.setRecipeName("Pasta");
        testRecipe.setServings(4);

        shoppinglist.backend.entity.UnitEntity testUnit = new shoppinglist.backend.entity.UnitEntity();
        testUnit.setUnitName("kg");

        shoppinglist.backend.entity.CategoryEntity testCategory = new shoppinglist.backend.entity.CategoryEntity();
        testCategory.setCategoryName("Vegetables");

        testItem = new ItemEntity();
        testItem.setId(1);
        testItem.setItemName("Tomato");
        testItem.setUnit(testUnit);
        testItem.setCategory(testCategory);
        testRecipeItem = new RecipeItemEntity();
        testRecipeItem.setId(1);
        testRecipeItem.setRecipe(testRecipe);
        testRecipeItem.setItem(testItem);
        testRecipeItem.setQuantity(2);

        testRecipeItemDto = new RecipeItemDto("Pasta", "Tomato", "kg", "Vegetables", 2);
    }

    @Test
    @DisplayName("Should get all recipe items")
    void testGetAllRecipeItems() {
        when(recipeItemRepository.findAll()).thenReturn(Collections.singletonList(testRecipeItem));

        List<RecipeItemDto> result = recipeItemService.getAllRecipeItems();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(recipeItemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get items for a recipe")
    void testGetItemsForRecipe_Success() throws IOException {
        when(recipeService.getSingleRecipe("Pasta")).thenReturn(testRecipe);
        when(recipeItemRepository.findByRecipe(testRecipe)).thenReturn(Arrays.asList(testRecipeItem));

        List<RecipeItemEntity> result = recipeItemService.getItemsForRecipe("Pasta");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(recipeService, times(1)).getSingleRecipe("Pasta");
        verify(recipeItemRepository, times(1)).findByRecipe(testRecipe);
    }

    @Test
    @DisplayName("Should throw exception when getting items for non-existent recipe")
    void testGetItemsForRecipe_NotFound() throws IOException {
        when(recipeService.getSingleRecipe("Pasta")).thenThrow(new IOException("Recipe doesn't exist!"));

        assertThrows(IOException.class, () -> recipeItemService.getItemsForRecipe("Pasta"));
    }

    @Test
    @DisplayName("Should add items to recipe successfully")
    void testAddItemsToRecipe_Success() throws IOException {
        List<RecipeItemDto> items = Arrays.asList(testRecipeItemDto);

        when(recipeService.getSingleRecipe("Pasta")).thenReturn(testRecipe);
        when(recipeItemRepository.findAll()).thenReturn(Arrays.asList());
        when(itemService.getSingleItemByNameAndUnit("Tomato", "kg")).thenReturn(testItem);
        when(recipeItemRepository.saveAll(any())).thenReturn(Arrays.asList(testRecipeItem));

        List<RecipeItemDto> result = recipeItemService.addItemsToRecipe(items);

        assertNotNull(result);
        verify(recipeService, times(2)).getSingleRecipe("Pasta");
        verify(recipeItemRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Should throw exception when adding items to non-existent recipe")
    void testAddItemsToRecipe_RecipeNotFound() throws IOException {
        List<RecipeItemDto> items = Arrays.asList(testRecipeItemDto);

        when(recipeService.getSingleRecipe("Pasta")).thenThrow(new IOException("Recipe doesn't exist!"));

        assertThrows(IOException.class, () -> recipeItemService.addItemsToRecipe(items));
    }

    @Test
    @DisplayName("Should throw exception when adding items to multiple recipes")
    void testAddItemsToRecipe_MultipleRecipes() throws IOException {
        RecipeItemDto dto1 = new RecipeItemDto("Pasta", "Tomato", "kg", "Vegetables", 2);
        RecipeItemDto dto2 = new RecipeItemDto("Pizza", "Tomato", "kg", "Vegetables", 2);
        List<RecipeItemDto> items = Arrays.asList(dto1, dto2);

        assertThrows(IOException.class, () -> recipeItemService.addItemsToRecipe(items));
        verify(recipeService, never()).getSingleRecipe(any());
    }

    @Test
    @DisplayName("Should delete recipe items successfully")
    void testDeleteRecipeItem_Success() throws IOException {
        List<RecipeItemDto> items = Arrays.asList(testRecipeItemDto);

        when(recipeService.getSingleRecipe("Pasta")).thenReturn(testRecipe);
        when(itemService.getSingleItemByNameAndUnit("Tomato", "kg")).thenReturn(testItem);
        when(recipeItemRepository.findByRecipeAndItem(testRecipe, testItem)).thenReturn(testRecipeItem);

        List<RecipeItemDto> result = recipeItemService.deleteRecipeItem(items);

        assertNotNull(result);
        verify(recipeService, times(1)).getSingleRecipe("Pasta");
        verify(recipeItemRepository, times(1)).deleteAll(any());
    }

    @Test
    @DisplayName("Should throw exception when deleting items from non-existent recipe")
    void testDeleteRecipeItem_RecipeNotFound() throws IOException {
        List<RecipeItemDto> items = Arrays.asList(testRecipeItemDto);

        when(recipeService.getSingleRecipe("Pasta")).thenThrow(new IOException("Recipe doesn't exist!"));

        assertThrows(IOException.class, () -> recipeItemService.deleteRecipeItem(items));
    }

    @Test
    @DisplayName("Should throw exception when deleting items from multiple recipes")
    void testDeleteRecipeItem_MultipleRecipes() throws IOException {
        RecipeItemDto dto1 = new RecipeItemDto("Pasta", "Tomato", "kg", "Vegetables", 2);
        RecipeItemDto dto2 = new RecipeItemDto("Pizza", "Tomato", "kg", "Vegetables", 2);
        List<RecipeItemDto> items = Arrays.asList(dto1, dto2);

        assertThrows(IOException.class, () -> recipeItemService.deleteRecipeItem(items));
    }

    @Test
    @DisplayName("Should update recipe items successfully")
    void testUpdateRecipeItem_Success() throws IOException {
        List<RecipeItemDto> items = Arrays.asList(testRecipeItemDto);

        when(recipeService.getSingleRecipe("Pasta")).thenReturn(testRecipe);
        when(recipeItemRepository.findByRecipe(testRecipe)).thenReturn(Arrays.asList(testRecipeItem));
        when(itemService.getSingleItemByNameAndUnit("Tomato", "kg")).thenReturn(testItem);
        when(recipeItemRepository.saveAll(any())).thenReturn(Arrays.asList(testRecipeItem));

        List<RecipeItemDto> result = recipeItemService.updateRecipeItem(items);

        assertNotNull(result);
        verify(recipeService, times(2)).getSingleRecipe("Pasta");
        verify(recipeItemRepository, times(1)).deleteAll(any());
        verify(recipeItemRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Should throw exception when updating items in non-existent recipe")
    void testUpdateRecipeItem_RecipeNotFound() throws IOException {
        List<RecipeItemDto> items = Arrays.asList(testRecipeItemDto);

        when(recipeService.getSingleRecipe("Pasta")).thenThrow(new IOException("Recipe doesn't exist"));

        assertThrows(IOException.class, () -> recipeItemService.updateRecipeItem(items));
    }

    @Test
    @DisplayName("Should throw exception when updating items in multiple recipes")
    void testUpdateRecipeItem_MultipleRecipes() throws IOException {
        RecipeItemDto dto1 = new RecipeItemDto("Pasta", "Tomato", "kg", "Vegetables", 2);
        RecipeItemDto dto2 = new RecipeItemDto("Pizza", "Tomato", "kg", "Vegetables", 2);
        List<RecipeItemDto> items = Arrays.asList(dto1, dto2);

        assertThrows(IOException.class, () -> recipeItemService.updateRecipeItem(items));
    }

    @Test
    @DisplayName("Should get single recipe item successfully")
    void testGetSingleRecipeItem_Success() throws IOException {
        when(recipeService.getSingleRecipe("Pasta")).thenReturn(testRecipe);
        when(itemService.getSingleItemByNameAndUnit("Tomato", "kg")).thenReturn(testItem);
        when(recipeItemRepository.findByRecipeAndItem(testRecipe, testItem)).thenReturn(testRecipeItem);

        RecipeItemEntity result = recipeItemService.getSingleRecipeItem("Pasta", "Tomato", "kg");

        assertNotNull(result);
        assertEquals(2, result.getQuantity());
        verify(recipeService, times(1)).getSingleRecipe("Pasta");
        verify(itemService, times(1)).getSingleItemByNameAndUnit("Tomato", "kg");
        verify(recipeItemRepository, times(1)).findByRecipeAndItem(testRecipe, testItem);
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent recipe item")
    void testGetSingleRecipeItem_NotFound() throws IOException {
        when(recipeService.getSingleRecipe("Pasta")).thenReturn(testRecipe);
        when(itemService.getSingleItemByNameAndUnit("Tomato", "kg")).thenReturn(testItem);
        when(recipeItemRepository.findByRecipeAndItem(testRecipe, testItem)).thenReturn(null);

        assertThrows(IOException.class, () -> recipeItemService.getSingleRecipeItem("Pasta", "Tomato", "kg"));
    }
}
