package shoppinglist.backend.service;

import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import shoppinglist.backend.dto.RecipeDto;
import shoppinglist.backend.entity.RecipeEntity;
import shoppinglist.backend.repository.RecipeRepository;

@Service
public class RecipeService {
  // CRUD operations for recipeEntities
  private final RecipeRepository recipeRepository;
  public RecipeService(RecipeRepository recipeRepository) {
    this.recipeRepository = recipeRepository;
  }

  // Get all recipes
  public List<RecipeDto> getAllRecipes() {
    return recipeRepository.findAll().stream().map(recipeEntity -> new RecipeDto(recipeEntity.getRecipeName(), recipeEntity.getServings())).toList();
  }
  // Add a recipe
  public RecipeDto addRecipe(RecipeDto recipeDto) throws IOException {

    if(recipeRepository.findByRecipeName(recipeDto.getName()) != null) {
      throw new IOException("Recipe already exists!");
    }
    RecipeEntity recipe = new RecipeEntity();
    recipe.setRecipeName(recipeDto.getName());
    recipe.setServings(recipeDto.getServings());
    recipeRepository.save(recipe);
    return recipeDto;
  }
  // Delete a recipe
  public RecipeDto deleteRecipe(RecipeDto recipeDto) throws IOException {
    RecipeEntity recipe = recipeRepository.findByRecipeName(recipeDto.getName());
    if (recipe == null) {
      throw new IOException("Recipe doesn't exist!");
    }
    recipeRepository.delete(recipe);
    return recipeDto;
  }

  // Update a recipe
  public RecipeDto updateRecipe(RecipeDto oldRecipe, RecipeDto newRecipe) throws IOException {
    RecipeEntity oldEntry = recipeRepository.findByRecipeName(oldRecipe.getName());
    RecipeEntity newEntry = recipeRepository.findByRecipeName(newRecipe.getName());
    if (oldEntry == null || newEntry != null) {
      throw new IOException("Original recipe doesn't exist!");
    }
    oldEntry.setRecipeName(newRecipe.getName());
    oldEntry.setServings(newRecipe.getServings());
    recipeRepository.save(oldEntry);
    return newRecipe;
  }

  public RecipeEntity getSingleRecipe(String recipeName) throws IOException {
    RecipeEntity recipe = recipeRepository.findByRecipeName(recipeName);
    if (recipe == null) {
      throw new IOException("Recipe doesn't exist!");
    }
    return recipe;
  }
}
