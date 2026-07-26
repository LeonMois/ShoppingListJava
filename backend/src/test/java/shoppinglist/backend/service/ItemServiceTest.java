package shoppinglist.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import shoppinglist.backend.dto.ItemDto;
import shoppinglist.backend.entity.CategoryEntity;
import shoppinglist.backend.entity.ItemEntity;
import shoppinglist.backend.entity.UnitEntity;
import shoppinglist.backend.repository.ItemRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("ItemService Tests")
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UnitService unitService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ItemService itemService;

    private ItemEntity testItem;
    private ItemDto testItemDto;
    private UnitEntity testUnit;
    private CategoryEntity testCategory;

    @BeforeEach
    void setUp() {

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

        testItemDto = new ItemDto("Apple", "Fruits", "kg");
    }

    @Test
    @DisplayName("Should get all items")
    void testGetAllItems() {
        ItemEntity item2 = new ItemEntity();
        item2.setItemName("Banana");
        item2.setUnit(testUnit);
        item2.setCategory(testCategory);

        when(itemRepository.findAll()).thenReturn(Arrays.asList(testItem, item2));

        List<ItemDto> result = itemService.getAllItems();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should add a new item successfully")
    void testAddItem_Success() throws IOException {
        when(unitService.getOrAddUnit("kg")).thenReturn(testUnit);
        when(itemRepository.findByItemNameIgnoreCaseAndUnit("Apple", testUnit)).thenReturn(null);
        when(categoryService.getOrAddCategory("Fruits")).thenReturn(testCategory);
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(testItem);

        ItemDto result = itemService.addItem(testItemDto);

        assertNotNull(result);
        assertEquals("Apple", result.getName());
        verify(unitService, times(1)).getOrAddUnit("kg");
        verify(categoryService, times(1)).getOrAddCategory("Fruits");
        verify(itemRepository, times(1)).save(any(ItemEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when adding duplicate item")
    void testAddItem_Duplicate() {
        when(unitService.getOrAddUnit("kg")).thenReturn(testUnit);
        when(itemRepository.findByItemNameIgnoreCaseAndUnit("Apple", testUnit)).thenReturn(testItem);

        assertThrows(IOException.class, () -> itemService.addItem(testItemDto));
        verify(unitService, times(1)).getOrAddUnit("kg");
        verify(itemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should add multiple items with some duplicates")
    void testAddItemList() throws IOException {
        List<ItemDto> itemList = Arrays.asList(
            new ItemDto("Apple", "Fruits", "kg"),
            new ItemDto("Banana", "Fruits", "kg"),
            new ItemDto("Apple", "Fruits", "kg")
        );

        ItemEntity item2 = new ItemEntity();
        item2.setItemName("Banana");
        item2.setUnit(testUnit);
        item2.setCategory(testCategory);

        when(unitService.getOrAddUnit("kg")).thenReturn(testUnit);
        when(itemRepository.findByItemNameIgnoreCaseAndUnit("Apple", testUnit))
            .thenReturn(null)   // first "Apple" — not found, will be saved
            .thenReturn(testItem); // second "Apple" — already exists, throws
        when(itemRepository.findByItemNameIgnoreCaseAndUnit("Banana", testUnit)).thenReturn(null);
        when(categoryService.getOrAddCategory("Fruits")).thenReturn(testCategory);
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(testItem).thenReturn(item2);
        when(itemRepository.findAll()).thenReturn(Arrays.asList(testItem, item2));

        List<ItemDto> result = itemService.addItemList(itemList);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(itemRepository, times(2)).findByItemNameIgnoreCaseAndUnit("Apple", testUnit);
        verify(itemRepository, times(1)).findByItemNameIgnoreCaseAndUnit("Banana", testUnit);
        verify(itemRepository, times(2)).save(any(ItemEntity.class));
        verify(itemRepository).findAll();
    }

    @Test
    @DisplayName("Should delete an item successfully")
    void testDeleteItem_Success() throws IOException {
        when(unitService.getUnit("kg")).thenReturn(testUnit);
        when(itemRepository.findByItemNameIgnoreCaseAndUnit("Apple", testUnit)).thenReturn(testItem);

        ItemDto result = itemService.deleteItem(testItemDto);

        assertNotNull(result);
        assertEquals("Apple", result.getName());
        verify(unitService, times(1)).getUnit("kg");
        verify(itemRepository, times(1)).delete(testItem);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent item")
    void testDeleteItem_NotFound() {
        when(unitService.getUnit("kg")).thenReturn(testUnit);
        when(itemRepository.findByItemNameIgnoreCaseAndUnit("Apple", testUnit)).thenReturn(null);

        assertThrows(IOException.class, () -> itemService.deleteItem(testItemDto));
        verify(itemRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should update an item successfully")
    void testUpdateItem_Success() throws IOException {
        ItemEntity updatedItem = new ItemEntity();
        updatedItem.setId(1);
        updatedItem.setItemName("AppleRed");
        updatedItem.setUnit(testUnit);
        updatedItem.setCategory(testCategory);

        ItemDto oldItemDto = new ItemDto("Apple", "Fruits", "kg");
        ItemDto newItemDto = new ItemDto("AppleRed", "Fruits", "kg");

        when(unitService.getUnit("kg")).thenReturn(testUnit);
        when(itemRepository.findByItemNameIgnoreCaseAndUnit("Apple", testUnit)).thenReturn(testItem);
        when(itemRepository.findByItemNameIgnoreCaseAndUnit("AppleRed", testUnit)).thenReturn(null);
        when(unitService.getOrAddUnit("kg")).thenReturn(testUnit);
        when(categoryService.getOrAddCategory("Fruits")).thenReturn(testCategory);
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(updatedItem);

        ItemDto result = itemService.updateItem(oldItemDto, newItemDto);

        assertNotNull(result);
        verify(itemRepository).save(any(ItemEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent item")
    void testUpdateItem_NotFound() {
        ItemDto oldItemDto = new ItemDto("Apple", "Fruits", "kg");
        ItemDto newItemDto = new ItemDto("AppleRed", "Fruits", "kg");

        when(unitService.getUnit("kg")).thenReturn(testUnit);
        when(itemRepository.findByItemNameIgnoreCaseAndUnit("Apple", testUnit)).thenReturn(null);

        assertThrows(IOException.class, () -> itemService.updateItem(oldItemDto, newItemDto));
        verify(itemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when updating to existing item name")
    void testUpdateItem_DuplicateName() {
        ItemEntity existingItem = new ItemEntity();
        existingItem.setId(2);
        existingItem.setItemName("Banana");
        existingItem.setUnit(testUnit);

        ItemDto oldItemDto = new ItemDto("Apple", "Fruits", "kg");
        ItemDto newItemDto = new ItemDto("Banana", "Fruits", "kg");

        when(unitService.getUnit("kg")).thenReturn(testUnit);
        when(itemRepository.findByItemNameIgnoreCaseAndUnit("Apple", testUnit)).thenReturn(testItem);
        when(itemRepository.findByItemNameIgnoreCaseAndUnit("Banana", testUnit)).thenReturn(existingItem);

        assertThrows(IOException.class, () -> itemService.updateItem(oldItemDto, newItemDto));
        verify(itemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get single item by name and unit")
    void testGetSingleItemByNameAndUnit_Success() throws IOException {
        when(unitService.getUnit("kg")).thenReturn(testUnit);
        when(itemRepository.findByItemNameIgnoreCaseAndUnit("Apple", testUnit)).thenReturn(testItem);

        ItemEntity result = itemService.getSingleItemByNameAndUnit("Apple", "kg");

        assertNotNull(result);
        assertEquals("Apple", result.getItemName());
        verify(itemRepository, times(1)).findByItemNameIgnoreCaseAndUnit("Apple", testUnit);
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent item by name and unit")
    void testGetSingleItemByNameAndUnit_NotFound() {
        when(unitService.getUnit("kg")).thenReturn(testUnit);
        when(itemRepository.findByItemNameIgnoreCaseAndUnit("Apple", testUnit)).thenReturn(null);

        assertThrows(IOException.class, () -> itemService.getSingleItemByNameAndUnit("Apple", "kg"));
    }

    @Test
    @DisplayName("Should get single item by name")
    void testGetSingleItemByName_Success() throws IOException {
        when(itemRepository.findByItemNameIgnoreCase("Apple")).thenReturn(testItem);

        ItemEntity result = itemService.getSingleItemByName("Apple");

        assertNotNull(result);
        assertEquals("Apple", result.getItemName());
        verify(itemRepository, times(1)).findByItemNameIgnoreCase("Apple");
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent item by name")
    void testGetSingleItemByName_NotFound() {
        when(itemRepository.findByItemNameIgnoreCase("Apple")).thenReturn(null);

        assertThrows(IOException.class, () -> itemService.getSingleItemByName("Apple"));
    }
}
