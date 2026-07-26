package shoppinglist.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import shoppinglist.backend.dto.CategoryDto;
import shoppinglist.backend.entity.CategoryEntity;
import shoppinglist.backend.repository.CategoryRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CategoryService Tests")
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryEntity testCategory;
    private CategoryDto testCategoryDto;

    @BeforeEach
    void setUp() {

        testCategory = new CategoryEntity();
        testCategory.setId(1);
        testCategory.setCategoryName("Fruits");

        testCategoryDto = new CategoryDto("Fruits");
    }

    @Test
    @DisplayName("Should add a new category successfully")
    void testAddCategory_Success() throws IOException {
        when(categoryRepository.findEntityByCategoryNameIgnoreCase("Fruits")).thenReturn(null);
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(testCategory);

        CategoryDto result = categoryService.addCategory("Fruits");

        assertNotNull(result);
        assertEquals("Fruits", result.getCategoryName());
        verify(categoryRepository, times(1)).findEntityByCategoryNameIgnoreCase("Fruits");
        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when adding duplicate category")
    void testAddCategory_Duplicate() {
        when(categoryRepository.findEntityByCategoryNameIgnoreCase("Fruits")).thenReturn(testCategory);

        assertThrows(IOException.class, () -> categoryService.addCategory("Fruits"));
        verify(categoryRepository, times(1)).findEntityByCategoryNameIgnoreCase("Fruits");
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should add multiple categories with some duplicates")
    void testAddCategoryList() throws IOException {
        List<CategoryDto> categoryList = Arrays.asList(
            new CategoryDto("Fruits"),
            new CategoryDto("Vegetables"),
            new CategoryDto("Fruits")
        );

        CategoryEntity cat1 = new CategoryEntity();
        cat1.setCategoryName("Fruits");
        CategoryEntity cat2 = new CategoryEntity();
        cat2.setCategoryName("Vegetables");

        when(categoryRepository.findEntityByCategoryNameIgnoreCase("Fruits"))
            .thenReturn(null)   // first "Fruits" — not found, will be saved
            .thenReturn(cat1);  // second "Fruits" — already exists, throws
        when(categoryRepository.findEntityByCategoryNameIgnoreCase("Vegetables"))
            .thenReturn(null);  // "Vegetables" — not found, will be saved
        when(categoryRepository.save(any(CategoryEntity.class)))
            .thenReturn(cat1)
            .thenReturn(cat2);
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

        List<CategoryDto> result = categoryService.addCategoryList(categoryList);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoryRepository, times(2)).findEntityByCategoryNameIgnoreCase("Fruits");
        verify(categoryRepository, times(1)).findEntityByCategoryNameIgnoreCase("Vegetables");
        verify(categoryRepository, times(2)).save(any(CategoryEntity.class));
        verify(categoryRepository).findAll();
    }

    @Test
    @DisplayName("Should delete a category successfully")
    void testDeleteCategory_Success() throws IOException {
        when(categoryRepository.findEntityByCategoryNameIgnoreCase("Fruits")).thenReturn(testCategory);

        CategoryDto result = categoryService.deleteCategory("Fruits");

        assertNotNull(result);
        assertEquals("Fruits", result.getCategoryName());
        verify(categoryRepository, times(1)).findEntityByCategoryNameIgnoreCase("Fruits");
        verify(categoryRepository, times(1)).delete(testCategory);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent category")
    void testDeleteCategory_NotFound() {
        when(categoryRepository.findEntityByCategoryNameIgnoreCase("Fruits")).thenReturn(null);

        assertThrows(IOException.class, () -> categoryService.deleteCategory("Fruits"));
        verify(categoryRepository, times(1)).findEntityByCategoryNameIgnoreCase("Fruits");
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should update category name successfully")
    void testUpdateCategory_Success() throws IOException {
        when(categoryRepository.findEntityByCategoryNameIgnoreCase("Fruits")).thenReturn(testCategory);
        when(categoryRepository.findEntityByCategoryNameIgnoreCase("Berries")).thenReturn(null);
        when(categoryRepository.save(testCategory)).thenReturn(testCategory);

        CategoryDto result = categoryService.updateCategory("Fruits", "Berries");

        assertNotNull(result);
        verify(categoryRepository).findEntityByCategoryNameIgnoreCase("Fruits");
        verify(categoryRepository).save(testCategory);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent category")
    void testUpdateCategory_NotFound() {
        when(categoryRepository.findEntityByCategoryNameIgnoreCase("Fruits")).thenReturn(null);

        assertThrows(IOException.class, () -> categoryService.updateCategory("Fruits", "Berries"));
        verify(categoryRepository).findEntityByCategoryNameIgnoreCase("Fruits");
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when updating to existing category name")
    void testUpdateCategory_DuplicateName() {
        CategoryEntity existingCategory = new CategoryEntity();
        existingCategory.setId(2);
        existingCategory.setCategoryName("Berries");

        when(categoryRepository.findEntityByCategoryNameIgnoreCase("Fruits")).thenReturn(testCategory);
        when(categoryRepository.findEntityByCategoryNameIgnoreCase("Berries")).thenReturn(existingCategory);

        assertThrows(IOException.class, () -> categoryService.updateCategory("Fruits", "Berries"));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get all categories")
    void testGetAllCategories() {
        CategoryEntity cat2 = new CategoryEntity();
        cat2.setCategoryName("Vegetables");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(testCategory, cat2));

        List<CategoryDto> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Fruits", result.get(0).getCategoryName());
        assertEquals("Vegetables", result.get(1).getCategoryName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get or add category - existing category")
    void testGetOrAddCategory_Existing() {
        when(categoryRepository.findEntityByCategoryNameIgnoreCase("Fruits")).thenReturn(testCategory);

        CategoryEntity result = categoryService.getOrAddCategory("Fruits");

        assertNotNull(result);
        assertEquals("Fruits", result.getCategoryName());
        verify(categoryRepository, times(1)).findEntityByCategoryNameIgnoreCase("Fruits");
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get or add category - new category")
    void testGetOrAddCategory_New() {
        when(categoryRepository.findEntityByCategoryNameIgnoreCase("Fruits"))
            .thenReturn(null)
            .thenReturn(testCategory);
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(testCategory);

        CategoryEntity result = categoryService.getOrAddCategory("Fruits");

        assertNotNull(result);
        assertEquals("Fruits", result.getCategoryName());
        verify(categoryRepository, times(2)).findEntityByCategoryNameIgnoreCase("Fruits");
        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }
}
