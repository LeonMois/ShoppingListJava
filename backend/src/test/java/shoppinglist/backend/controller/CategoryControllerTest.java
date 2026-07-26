package shoppinglist.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import shoppinglist.backend.dto.CategoryDto;
import shoppinglist.backend.service.CategoryService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@DisplayName("CategoryController Tests")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    @DisplayName("GET /category should return all categories")
    void testGetCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of(new CategoryDto("Fruits")));

        mockMvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryName").value("Fruits"));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    @DisplayName("POST /category/add should add a category")
    void testAddCategory() throws Exception {
        CategoryDto category = new CategoryDto("Fruits");
        when(categoryService.addCategory("Fruits")).thenReturn(category);

        mockMvc.perform(post("/category/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Fruits"));

        verify(categoryService, times(1)).addCategory("Fruits");
    }

    @Test
    @DisplayName("POST /category/addList should add a list of categories")
    void testAddCategoryList() throws Exception {
        CategoryDto category1 = new CategoryDto("Fruits");
        CategoryDto category2 = new CategoryDto("Vegetables");
        when(categoryService.addCategoryList(anyList())).thenReturn(Arrays.asList(category1, category2));

        mockMvc.perform(post("/category/addList")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Arrays.asList(category1, category2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(categoryService, times(1)).addCategoryList(anyList());
    }

    @Test
    @DisplayName("PUT /category/update should update a category")
    void testUpdateCategory() throws Exception {
        CategoryDto oldCategory = new CategoryDto("Fruits");
        CategoryDto newCategory = new CategoryDto("FreshFruits");
        when(categoryService.updateCategory("Fruits", "FreshFruits")).thenReturn(newCategory);

        mockMvc.perform(put("/category/update")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Arrays.asList(oldCategory, newCategory))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("FreshFruits"));

        verify(categoryService, times(1)).updateCategory("Fruits", "FreshFruits");
    }

    @Test
    @DisplayName("DELETE /category/delete should delete a category")
    void testDeleteCategory() throws Exception {
        CategoryDto category = new CategoryDto("Fruits");
        when(categoryService.deleteCategory("Fruits")).thenReturn(category);

        mockMvc.perform(delete("/category/delete")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Fruits"));

        verify(categoryService, times(1)).deleteCategory("Fruits");
    }

    @Test
    @DisplayName("DELETE /category/delete should propagate IOException for missing category")
    void testDeleteCategory_ThrowsIOException() throws Exception {
        CategoryDto category = new CategoryDto("Fruits");
        when(categoryService.deleteCategory("Fruits")).thenThrow(new IOException("Category not found"));

        assertThrows(IOException.class, () -> mockMvc.perform(delete("/category/delete")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(category))));
    }
}
