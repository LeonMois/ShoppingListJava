package shoppinglist.backend.service;

import org.springframework.stereotype.Service;
import shoppinglist.backend.dto.RecipeItemDto;
import shoppinglist.backend.entity.ItemEntity;
import shoppinglist.backend.entity.RecipeEntity;
import shoppinglist.backend.entity.RecipeItemEntity;
import shoppinglist.backend.repository.RecipeItemRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public List<RecipeItemEntity> getItemsForRecipe(String recipeName) throws IOException {
        RecipeEntity recipe = recipeService.getSingleRecipe(recipeName);
        return recipeItemRepository.findByRecipe(recipe);
    }

    // Add a recipeItem
    public List<RecipeItemDto> addItemsToRecipe(List<RecipeItemDto> recipeItems) throws IOException {
        if (recipeItems.stream().map(RecipeItemDto::getRecipeName).distinct().count() > 1) {
            throw new IOException("Changing more than one recipe at a time is not allowed!");
        }
        RecipeEntity recipe = recipeService.getSingleRecipe(recipeItems.getFirst().getRecipeName());
        if (recipe == null) {
            throw new IOException("Recipe doesn't exist!");
        }
        recipeItems.removeAll(recipeItemRepository.findAll().stream().map(RecipeItemEntity::mapToDto).toList());
        List<RecipeItemEntity> saveEntities = new ArrayList<>();
        for (RecipeItemDto item : recipeItems) {
            RecipeItemEntity newEntity = createEntityFromDto(item);
            saveEntities.add(newEntity);
        }
        saveEntities = recipeItemRepository.saveAll(saveEntities);
        return saveEntities.stream().map(RecipeItemEntity::mapToDto).toList();
    }

    // Delete a recipeItem
    public List<RecipeItemDto> deleteRecipeItem(List<RecipeItemDto> recipeItems) throws IOException {
        List<RecipeItemEntity> deleteEntities = new ArrayList<>();
        for (RecipeItemDto recipeItem : recipeItems) {
            RecipeEntity recipe = recipeService.getSingleRecipe(recipeItem.getRecipeName());
            ItemEntity item = itemService.getSingleItemByNameAndUnit(recipeItem.getItemName(), recipeItem.getUnit());
            deleteEntities.add(recipeItemRepository.findByRecipeAndItem(recipe, item));
        }
        if (!deleteEntities.isEmpty()) {
            recipeItemRepository.deleteAll(deleteEntities);
        }
        return deleteEntities.stream().map(RecipeItemEntity::mapToDto).toList();
    }

    public List<RecipeItemDto> updateRecipeItem(List<RecipeItemDto> recipeItems) throws IOException {
        if (recipeItems.stream().map(RecipeItemDto::getRecipeName).distinct().count() > 1) {
            throw new IOException("Changing more than one recipe at a time is not allowed!");
        }
        RecipeEntity oldRecipe = recipeService.getSingleRecipe(recipeItems.getFirst().getRecipeName());
        if (oldRecipe == null) {
            throw new IOException("Recipe doesn't exist");
        }
        List<RecipeItemEntity> createEntities = new ArrayList<>();
        for (RecipeItemDto recipeItem : recipeItems) {
            createEntities.add(createEntityFromDto(recipeItem));
        }
        if (!createEntities.isEmpty()) {
            recipeItemRepository.deleteAll(recipeItemRepository.findByRecipe(oldRecipe));
            createEntities = recipeItemRepository.saveAll(createEntities);
        }
        return createEntities.stream().map(RecipeItemEntity::mapToDto).toList();
    }

    public RecipeItemEntity getSingleRecipeItem(String recipeName, String itemName, String unit) throws IOException {
        RecipeEntity recipe = recipeService.getSingleRecipe(recipeName);
        ItemEntity item = itemService.getSingleItemByNameAndUnit(itemName, unit);
        RecipeItemEntity recipeItem = recipeItemRepository.findByRecipeAndItem(recipe, item);
        if (recipeItem == null) {
            throw new IOException("RecipeItem doesn't exist!");
        }
        return recipeItem;
    }

    private RecipeItemEntity createEntityFromDto(RecipeItemDto dto) throws IOException {
        RecipeItemEntity entity = new RecipeItemEntity();
        entity.setItem(itemService.getSingleItemByNameAndUnit(dto.getItemName(), dto.getUnit()));
        entity.setRecipe(recipeService.getSingleRecipe(dto.getRecipeName()));
        entity.setQuantity(dto.getQuantity());
        return entity;
    }
}
