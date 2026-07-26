package shoppinglist.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shoppinglist.backend.dto.RecipeDto;
import shoppinglist.backend.dto.ShoppingListDto;
import shoppinglist.backend.entity.CategoryEntity;
import shoppinglist.backend.entity.ItemEntity;
import shoppinglist.backend.entity.RecipeEntity;
import shoppinglist.backend.entity.RecipeItemEntity;
import shoppinglist.backend.entity.ShoppingListEntity;
import shoppinglist.backend.entity.UnitEntity;
import shoppinglist.backend.repository.ShoppingListRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("ShoppingListService Tests")
class ShoppingListServiceTest {

    @Mock
    private ShoppingListRepository shoppingListRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private RecipeItemService recipeItemService;

    @InjectMocks
    private ShoppingListService shoppingListService;

    private ShoppingListEntity testShoppingListItem;
    private ShoppingListDto testShoppingListDto;
    private ItemEntity testItem;
    private UnitEntity testUnit;
    private CategoryEntity testCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUnit = new UnitEntity();
        testUnit.setId(1);
        testUnit.setUnitName("kg");

        testCategory = new CategoryEntity();
        testCategory.setId(1);
        testCategory.setCategoryName("Fruits");

        testItem = new ItemEntity();
        testItem.setId(1);
        testItem.setItemName("Apple");
        testItem.setUnit(testUnit);
        testItem.setCategory(testCategory);

        testShoppingListItem = new ShoppingListEntity();
        testShoppingListItem.setId(1);
        testShoppingListItem.setItem(testItem);
        testShoppingListItem.setQuantity(5);
        testShoppingListItem.setDeleted(0);

        testShoppingListDto = new ShoppingListDto();
        testShoppingListDto.setItemName("Apple");
        testShoppingListDto.setUnitName("kg");
        testShoppingListDto.setQuantity(5);
        testShoppingListDto.setCategory("Fruits");
        testShoppingListDto.setDeleted(false);
    }

    @Test
    @DisplayName("Should get all shopping list items")
    void testGetAll() {
        when(shoppingListRepository.findAll()).thenReturn(Arrays.asList(testShoppingListItem));

        List<ShoppingListDto> result = shoppingListService.getAll(null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(shoppingListRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get all shopping list items with sort order")
    void testGetAll_WithSortOrder() {
        ShoppingListEntity item2 = new ShoppingListEntity();
        item2.setId(2);
        ItemEntity item2Entity = new ItemEntity();
        item2Entity.setItemName("Carrot");
        item2Entity.setUnit(testUnit);
        CategoryEntity catVeg = new CategoryEntity();
        catVeg.setCategoryName("Vegetables");
        item2Entity.setCategory(catVeg);
        item2.setItem(item2Entity);
        item2.setQuantity(3);
        item2.setDeleted(0);

        when(shoppingListRepository.findAll()).thenReturn(Arrays.asList(testShoppingListItem, item2));

        List<ShoppingListDto> result = shoppingListService.getAll("Vegetables");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("Should add items to shopping list successfully")
    void testAddItems_Success() throws IOException {
        List<ShoppingListDto> items = Arrays.asList(testShoppingListDto);

        when(itemService.getSingleItemByNameAndUnit("Apple", "kg")).thenReturn(testItem);
        when(shoppingListRepository.saveAll(any())).thenReturn(Arrays.asList(testShoppingListItem));

        List<ShoppingListDto> result = shoppingListService.addItems(items);

        assertNotNull(result);
        verify(itemService, times(1)).getSingleItemByNameAndUnit("Apple", "kg");
        verify(shoppingListRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Should throw exception when adding items with non-existent item")
    void testAddItems_ItemNotFound() throws IOException {
        List<ShoppingListDto> items = Arrays.asList(testShoppingListDto);

        when(itemService.getSingleItemByNameAndUnit("Apple", "kg")).thenThrow(new IOException("Item doesn't exist!"));

        assertThrows(IOException.class, () -> shoppingListService.addItems(items));
    }

    @Test
    @DisplayName("Should add multiple items to shopping list")
    void testAddItems_Multiple() throws IOException {
        ShoppingListDto item2 = new ShoppingListDto();
        item2.setItemName("Banana");
        item2.setUnitName("kg");
        item2.setQuantity(3);
        item2.setCategory("Fruits");

        List<ShoppingListDto> items = Arrays.asList(testShoppingListDto, item2);

        ItemEntity item2Entity = new ItemEntity();
        item2Entity.setItemName("Banana");
        item2Entity.setUnit(testUnit);
        item2Entity.setCategory(testCategory);

        ShoppingListEntity shoppingItem2 = new ShoppingListEntity();
        shoppingItem2.setItem(item2Entity);
        shoppingItem2.setQuantity(3);
        shoppingItem2.setDeleted(0);

        when(itemService.getSingleItemByNameAndUnit("Apple", "kg")).thenReturn(testItem);
        when(itemService.getSingleItemByNameAndUnit("Banana", "kg")).thenReturn(item2Entity);
        when(shoppingListRepository.saveAll(any())).thenReturn(Arrays.asList(testShoppingListItem, shoppingItem2));

        List<ShoppingListDto> result = shoppingListService.addItems(items);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should add recipes to shopping list successfully")
    void testAddRecipes_Success() throws IOException {
        RecipeDto recipe = new RecipeDto("Pasta", 2);
        List<RecipeDto> recipes = Arrays.asList(recipe);

        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeName("Pasta");
        recipeEntity.setServings(2);

        RecipeItemEntity recipeItem = new RecipeItemEntity();
        recipeItem.setItem(testItem);
        recipeItem.setQuantity(2);

        when(recipeItemService.getItemsForRecipe("Pasta")).thenReturn(Arrays.asList(recipeItem));
        when(shoppingListRepository.saveAll(any())).thenReturn(Arrays.asList(testShoppingListItem, testShoppingListItem));

        List<ShoppingListDto> result = shoppingListService.addRecipes(recipes);

        assertNotNull(result);
        verify(recipeItemService, times(2)).getItemsForRecipe("Pasta");
    }

    @Test
    @DisplayName("Should throw exception when adding non-existent recipe")
    void testAddRecipes_RecipeNotFound() throws IOException {
        RecipeDto recipe = new RecipeDto("NonExistent", 2);
        List<RecipeDto> recipes = Arrays.asList(recipe);

        when(recipeItemService.getItemsForRecipe("NonExistent")).thenThrow(new IOException("Recipe doesn't exist!"));

        assertThrows(IOException.class, () -> shoppingListService.addRecipes(recipes));
    }

    @Test
    @DisplayName("Should add recipe with multiple servings")
    void testAddRecipes_MultipleServings() throws IOException {
        RecipeDto recipe = new RecipeDto("Pasta", 3);
        List<RecipeDto> recipes = Arrays.asList(recipe);

        RecipeItemEntity recipeItem = new RecipeItemEntity();
        recipeItem.setItem(testItem);
        recipeItem.setQuantity(2);

        when(recipeItemService.getItemsForRecipe("Pasta")).thenReturn(Arrays.asList(recipeItem));
        when(shoppingListRepository.saveAll(any())).thenReturn(Arrays.asList(testShoppingListItem));

        List<ShoppingListDto> result = shoppingListService.addRecipes(recipes);

        assertNotNull(result);
        verify(recipeItemService, times(3)).getItemsForRecipe("Pasta");
    }

    @Test
    @DisplayName("Should set items to deleted successfully")
    void testSetItemsToDeleted_Success() throws IOException {
        List<ShoppingListDto> items = Arrays.asList(testShoppingListDto);

        when(itemService.getSingleItemByNameAndUnit("Apple", "kg")).thenReturn(testItem);
        when(shoppingListRepository.findByItem(testItem)).thenReturn(Arrays.asList(testShoppingListItem));
        when(shoppingListRepository.saveAll(any())).thenReturn(Arrays.asList(testShoppingListItem));

        List<ShoppingListDto> result = shoppingListService.setItemsToDeleted(items);

        assertNotNull(result);
        verify(shoppingListRepository).saveAll(any());
    }

    @Test
    @DisplayName("Should toggle deleted status when setting to deleted")
    void testSetItemsToDeleted_Toggle() throws IOException {
        ShoppingListEntity deletedItem = new ShoppingListEntity();
        deletedItem.setId(1);
        deletedItem.setItem(testItem);
        deletedItem.setQuantity(5);
        deletedItem.setDeleted(1);

        List<ShoppingListDto> items = Arrays.asList(testShoppingListDto);

        when(itemService.getSingleItemByNameAndUnit("Apple", "kg")).thenReturn(testItem);
        when(shoppingListRepository.findByItem(testItem)).thenReturn(Arrays.asList(deletedItem));
        when(shoppingListRepository.saveAll(any())).thenReturn(Arrays.asList(deletedItem));

        List<ShoppingListDto> result = shoppingListService.setItemsToDeleted(items);

        assertNotNull(result);
        verify(shoppingListRepository).saveAll(any());
    }

    @Test
    @DisplayName("Should remove items marked as deleted")
    void testRemoveItems() {
        ShoppingListEntity deletedItem = new ShoppingListEntity();
        deletedItem.setId(1);
        deletedItem.setItem(testItem);
        deletedItem.setQuantity(5);
        deletedItem.setDeleted(1);

        when(shoppingListRepository.findByDeleted(1)).thenReturn(Arrays.asList(deletedItem));

        List<ShoppingListDto> result = shoppingListService.removeItems();

        assertNotNull(result);
        verify(shoppingListRepository, times(1)).findByDeleted(1);
        verify(shoppingListRepository, times(1)).deleteAll(any());
    }

    @Test
    @DisplayName("Should handle empty shopping list when removing items")
    void testRemoveItems_Empty() {
        when(shoppingListRepository.findByDeleted(1)).thenReturn(Arrays.asList());

        List<ShoppingListDto> result = shoppingListService.removeItems();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(shoppingListRepository).findByDeleted(1);
        verify(shoppingListRepository).deleteAll(any());
    }

    @Test
    @DisplayName("Should handle null sort order in getAll")
    void testGetAll_NullSortOrder() {
        when(shoppingListRepository.findAll()).thenReturn(Arrays.asList(testShoppingListItem));

        List<ShoppingListDto> result = shoppingListService.getAll(null);

        assertNotNull(result);
        verify(shoppingListRepository).findAll();
    }
}
