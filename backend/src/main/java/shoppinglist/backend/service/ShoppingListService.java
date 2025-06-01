package shoppinglist.backend.service;

import org.springframework.stereotype.Service;
import shoppinglist.backend.dto.RecipeDto;
import shoppinglist.backend.dto.ShoppingListDto;
import shoppinglist.backend.entity.RecipeItemEntity;
import shoppinglist.backend.entity.ShoppingListEntity;
import shoppinglist.backend.repository.ShoppingListRepository;
import shoppinglist.backend.util.ItemUnit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

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


    public List<ShoppingListDto> getAll(final String sortOrder) {
        List<ShoppingListDto> items = shoppingListRepository.findAll().stream().map(ShoppingListEntity::mapToDto).toList();
        Map<ItemUnit, List<ShoppingListDto>> grouped = items.stream().collect(groupingBy(entry -> new ItemUnit(entry.getItemName(), entry.getUnitName())));
        List<ShoppingListDto> summedItems = new ArrayList<>();
        // Group equal types
        for (ItemUnit key : grouped.keySet()) {
            ShoppingListDto entry = new ShoppingListDto();
            entry.setQuantity(0);
            for (ShoppingListDto groupedItem : grouped.get(key)) {
                entry.setQuantity(entry.getQuantity() + groupedItem.getQuantity());
            }
            ShoppingListDto first = grouped.get(key).getFirst();
            entry.setUnitName(first.getUnitName());
            entry.setItemName(first.getItemName());
            entry.setDeleted(first.isDeleted());
            entry.setCategory(first.getCategory());
            summedItems.add(entry);
        }
        // Sort by sort order
        final String sortBy = sortOrder == null ? summedItems.getFirst().getCategory() : sortOrder;
        summedItems.sort((ShoppingListDto item1, ShoppingListDto item2) -> {
            int i1 = item1.getCategory().equals(sortBy) ? 1 : -1;
            int i2 = item2.getCategory().equals(sortBy) ? 1 : -1;
            return i1 > i2 ? -1 : 1;
        });
        return summedItems;
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
            entity.forEach(shoppingListEntity -> shoppingListEntity.setDeleted(shoppingListEntity.getDeleted() == 1 ? 0 : 1));
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
