package shoppinglist.backend.service;

import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import shoppinglist.backend.dto.RecipeItemDto;
import shoppinglist.backend.entity.ItemEntity;
import shoppinglist.backend.entity.RecipeEntity;
import shoppinglist.backend.entity.RecipeItemEntity;
import shoppinglist.backend.repository.RecipeItemRepository;

@Service
public class RecipeItemService {

  // CRUD operations for recipeItems
  private final RecipeItemRepository recipeItemRepository;
  private final RecipeService recipeService;
  private final ItemService itemService;

  public RecipeItemService(RecipeItemRepository recipeItemRepository, RecipeService recipeService, ItemService itemService) {
    this.recipeItemRepository = recipeItemRepository;
    this.recipeService = recipeService;
    this.itemService = itemService;
  }

  // Get all recipeItems
  public List<RecipeItemDto> getAllRecipeItems() {
    return recipeItemRepository.findAll().stream().map(
            recipeItemEntity -> new RecipeItemDto(recipeItemEntity.getRecipe().getRecipeName(), recipeItemEntity.getItem().getItemName(),
                recipeItemEntity.getItem().getUnit().getUnitName(), recipeItemEntity.getItem().getCategory().getCategoryName(), recipeItemEntity.getQuantity()))
        .toList();
  }

  // Add a recipeItem
  public RecipeItemDto addRecipeItem(RecipeItemDto recipeItemDto) throws IOException {
    RecipeEntity recipe = recipeService.getSingleRecipe(recipeItemDto.getRecipeName());
    ItemEntity item = itemService.getSingleItem(recipeItemDto.getItemName(), recipeItemDto.getUnit());
    if (recipeItemRepository.findByRecipeAndItem(recipe, item) != null) {
      throw new IOException("RecipeItem already exists!");
    }
    RecipeItemEntity recipeItem = new RecipeItemEntity();
    recipeItem.setRecipe(recipe);
    recipeItem.setItem(item);
    recipeItem.setQuantity(recipeItemDto.getQuantity());
    recipeItemRepository.save(recipeItem);
    return recipeItemDto;
  }

  // Delete a recipeItem
  public RecipeItemDto deleteRecipeItem(RecipeItemDto recipeItemDto) throws IOException {
    RecipeEntity recipe = recipeService.getSingleRecipe(recipeItemDto.getRecipeName());
    ItemEntity item = itemService.getSingleItem(recipeItemDto.getItemName(), recipeItemDto.getUnit());
    RecipeItemEntity recipeItem = recipeItemRepository.findByRecipeAndItem(recipe, item);
    if (recipeItem == null) {
      throw new IOException("RecipeItem doesnt exist!");
    }
    recipeItemRepository.delete(recipeItem);
    return recipeItemDto;
  }

  public RecipeItemDto updateRecipeItem(RecipeItemDto oldRecipeitemDto, RecipeItemDto newRecipeItemDto) throws IOException {
    RecipeEntity oldRecipe = recipeService.getSingleRecipe(oldRecipeitemDto.getRecipeName());
    ItemEntity oldItem = itemService.getSingleItem(oldRecipeitemDto.getItemName(), oldRecipeitemDto.getUnit());
    RecipeItemEntity oldRecipeItem = recipeItemRepository.findByRecipeAndItem(oldRecipe, oldItem);

    RecipeEntity newRecipe = recipeService.getSingleRecipe(newRecipeItemDto.getRecipeName());
    ItemEntity newItem = itemService.getSingleItem(newRecipeItemDto.getItemName(), newRecipeItemDto.getUnit());

    if (oldRecipeItem == null || recipeItemRepository.findByRecipeAndItem(newRecipe, newItem) != null) {
      throw new IOException("Original recipeItem doesn't exist!");
    }
    oldRecipeItem.setRecipe(newRecipe);
    oldRecipeItem.setItem(newItem);
    oldRecipeItem.setQuantity(newRecipeItemDto.getQuantity());
    recipeItemRepository.save(oldRecipeItem);
    return newRecipeItemDto;
  }

  public RecipeItemEntity getSingleRecipeItem(String recipeName, String itemName, String unit) throws IOException {
    RecipeEntity recipe = recipeService.getSingleRecipe(recipeName);
    ItemEntity item = itemService.getSingleItem(itemName, unit);
    RecipeItemEntity recipeItem = recipeItemRepository.findByRecipeAndItem(recipe, item);
    if (recipeItem == null) {
      throw new IOException("RecipeItem doesn't exist!");
    }
    return recipeItem;
  }
}
