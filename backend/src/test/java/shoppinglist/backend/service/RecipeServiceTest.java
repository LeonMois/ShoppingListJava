package shoppinglist.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shoppinglist.backend.dto.RecipeDto;
import shoppinglist.backend.entity.RecipeEntity;
import shoppinglist.backend.repository.RecipeRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("RecipeService Tests")
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    private RecipeEntity testRecipe;
    private RecipeDto testRecipeDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testRecipe = new RecipeEntity();
        testRecipe.setId(1);
        testRecipe.setRecipeName("Pasta");
        testRecipe.setServings(4);

        testRecipeDto = new RecipeDto("Pasta", 4);
    }

    @Test
    @DisplayName("Should get all recipes")
    void testGetAllRecipes() {
        RecipeEntity recipe2 = new RecipeEntity();
        recipe2.setRecipeName("Pizza");
        recipe2.setServings(2);

        when(recipeRepository.findAll()).thenReturn(Arrays.asList(testRecipe, recipe2));

        List<RecipeDto> result = recipeService.getAllRecipes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Pasta", result.get(0).getName());
        assertEquals("Pizza", result.get(1).getName());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should add a new recipe successfully")
    void testAddRecipe_Success() throws IOException {
        when(recipeRepository.findByRecipeNameIgnoreCase("Pasta")).thenReturn(null);
        when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(testRecipe);

        RecipeDto result = recipeService.addRecipe(testRecipeDto);

        assertNotNull(result);
        assertEquals("Pasta", result.getName());
        assertEquals(4, result.getServings());
        verify(recipeRepository, times(1)).findByRecipeNameIgnoreCase("Pasta");
        verify(recipeRepository, times(1)).save(any(RecipeEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when adding duplicate recipe")
    void testAddRecipe_Duplicate() {
        when(recipeRepository.findByRecipeNameIgnoreCase("Pasta")).thenReturn(testRecipe);

        assertThrows(IOException.class, () -> recipeService.addRecipe(testRecipeDto));
        verify(recipeRepository, times(1)).findByRecipeNameIgnoreCase("Pasta");
        verify(recipeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete a recipe successfully")
    void testDeleteRecipe_Success() throws IOException {
        when(recipeRepository.findByRecipeNameIgnoreCase("Pasta")).thenReturn(testRecipe);

        RecipeDto result = recipeService.deleteRecipe(testRecipeDto);

        assertNotNull(result);
        assertEquals("Pasta", result.getName());
        verify(recipeRepository, times(1)).findByRecipeNameIgnoreCase("Pasta");
        verify(recipeRepository, times(1)).delete(testRecipe);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent recipe")
    void testDeleteRecipe_NotFound() {
        when(recipeRepository.findByRecipeNameIgnoreCase("Pasta")).thenReturn(null);

        assertThrows(IOException.class, () -> recipeService.deleteRecipe(testRecipeDto));
        verify(recipeRepository, times(1)).findByRecipeNameIgnoreCase("Pasta");
        verify(recipeRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should update recipe successfully")
    void testUpdateRecipe_Success() throws IOException {
        RecipeEntity updatedRecipe = new RecipeEntity();
        updatedRecipe.setId(1);
        updatedRecipe.setRecipeName("Pasta");
        updatedRecipe.setServings(6);

        RecipeDto oldRecipeDto = new RecipeDto("Pasta", 4);
        RecipeDto newRecipeDto = new RecipeDto("Pasta", 6);

        when(recipeRepository.findByRecipeNameIgnoreCase("Pasta")).thenReturn(testRecipe);
        when(recipeRepository.save(testRecipe)).thenReturn(updatedRecipe);

        RecipeDto result = recipeService.updateRecipe(oldRecipeDto, newRecipeDto);

        assertNotNull(result);
        assertEquals("Pasta", result.getName());
        verify(recipeRepository, times(2)).findByRecipeNameIgnoreCase("Pasta");
        verify(recipeRepository, times(1)).save(testRecipe);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent recipe")
    void testUpdateRecipe_NotFound() {
        RecipeDto oldRecipeDto = new RecipeDto("Pasta", 4);
        RecipeDto newRecipeDto = new RecipeDto("Pasta", 6);

        when(recipeRepository.findByRecipeNameIgnoreCase("Pasta")).thenReturn(null);

        assertThrows(IOException.class, () -> recipeService.updateRecipe(oldRecipeDto, newRecipeDto));
        verify(recipeRepository).findByRecipeNameIgnoreCase("Pasta");
        verify(recipeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when updating to existing recipe name")
    void testUpdateRecipe_DuplicateName() {
        RecipeEntity existingRecipe = new RecipeEntity();
        existingRecipe.setId(2);
        existingRecipe.setRecipeName("Pizza");

        RecipeDto oldRecipeDto = new RecipeDto("Pasta", 4);
        RecipeDto newRecipeDto = new RecipeDto("Pizza", 4);

        when(recipeRepository.findByRecipeNameIgnoreCase("Pasta")).thenReturn(testRecipe);
        when(recipeRepository.findByRecipeNameIgnoreCase("Pizza")).thenReturn(existingRecipe);

        assertThrows(IOException.class, () -> recipeService.updateRecipe(oldRecipeDto, newRecipeDto));
        verify(recipeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get single recipe by name")
    void testGetSingleRecipe_Success() throws IOException {
        when(recipeRepository.findByRecipeNameIgnoreCase("Pasta")).thenReturn(testRecipe);

        RecipeEntity result = recipeService.getSingleRecipe("Pasta");

        assertNotNull(result);
        assertEquals("Pasta", result.getRecipeName());
        verify(recipeRepository, times(1)).findByRecipeNameIgnoreCase("Pasta");
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent recipe")
    void testGetSingleRecipe_NotFound() {
        when(recipeRepository.findByRecipeNameIgnoreCase("Pasta")).thenReturn(null);

        assertThrows(IOException.class, () -> recipeService.getSingleRecipe("Pasta"));
        verify(recipeRepository, times(1)).findByRecipeNameIgnoreCase("Pasta");
    }

    @Test
    @DisplayName("Should handle case-insensitive recipe name search")
    void testGetSingleRecipe_CaseInsensitive() throws IOException {
        when(recipeRepository.findByRecipeNameIgnoreCase("PASTA")).thenReturn(testRecipe);

        RecipeEntity result = recipeService.getSingleRecipe("PASTA");

        assertNotNull(result);
        assertEquals("Pasta", result.getRecipeName());
        verify(recipeRepository, times(1)).findByRecipeNameIgnoreCase("PASTA");
    }
}
