package shoppinglist.backend.service;

import org.springframework.stereotype.Service;
import shoppinglist.backend.dto.RecipeDto;
import shoppinglist.backend.dto.ShoppingListDto;
import shoppinglist.backend.entity.RecipeItemEntity;
import shoppinglist.backend.entity.ShoppingListEntity;
import shoppinglist.backend.repository.ShoppingListRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;

    private final RecipeItemService recipeItemService;
    private final ItemService itemService;

    public ShoppingListService(ShoppingListRepository shoppingListRepository, ItemService itemService, RecipeItemService recipeItemService) {
        this.shoppingListRepository = shoppingListRepository;
        this.itemService = itemService;
        this.recipeItemService = recipeItemService;
    }


    public List<ShoppingListDto> getAll() {
        return shoppingListRepository.findAll().stream().map(ShoppingListEntity::mapToDto).toList();
    }

    public List<ShoppingListDto> addItems(List<ShoppingListDto> items) throws IOException {
        List<ShoppingListEntity> saveEntities = new ArrayList<>();
        for (ShoppingListDto item : items) {
            ShoppingListEntity entity = new ShoppingListEntity();
            entity.setDeleted(0);
            entity.setItem(itemService.getSingleItemByNameAndUnit(item.getItemName(), item.getUnitName()));
            entity.setQuantity(item.getQuantity());
            saveEntities.add(entity);
        }
        saveEntities = shoppingListRepository.saveAll(saveEntities);
        return saveEntities.stream().map(ShoppingListEntity::mapToDto).toList();
    }

    public List<ShoppingListDto> addRecipes(List<RecipeDto> recipes) throws IOException {
        List<ShoppingListEntity> saveEntities = new ArrayList<>();
        for (RecipeDto recipe : recipes) {
            List<RecipeItemEntity> recipeItems = recipeItemService.getItemsForRecipe(recipe.getName());
            for (RecipeItemEntity item : recipeItems) {
                ShoppingListEntity entity = new ShoppingListEntity();
                entity.setDeleted(0);
                entity.setItem(item.getItem());
                entity.setQuantity(item.getQuantity());
                saveEntities.add(entity);

            }
        }
        saveEntities = shoppingListRepository.saveAll(saveEntities);
        return saveEntities.stream().map(ShoppingListEntity::mapToDto).toList();
    }

    public List<ShoppingListDto> setItemsToDeleted(List<ShoppingListDto> items) throws IOException {
        List<ShoppingListEntity> updateEntities = new ArrayList<>();
        for (ShoppingListDto item : items) {
            List<ShoppingListEntity> entity = shoppingListRepository.findByItem(itemService.getSingleItemByNameAndUnit(item.getItemName(), item.getUnitName()));
            entity.forEach(shoppingListEntity -> shoppingListEntity.setDeleted(1));
            updateEntities.addAll(entity);
        }
        return shoppingListRepository.saveAll(updateEntities).stream().map(ShoppingListEntity::mapToDto).toList();
    }

    public List<ShoppingListDto> removeItems() {
        List<ShoppingListEntity> deletedEntities = shoppingListRepository.findByDeleted(1);
        shoppingListRepository.deleteAll(deletedEntities);
        return deletedEntities.stream().map(ShoppingListEntity::mapToDto).toList();
    }
}
