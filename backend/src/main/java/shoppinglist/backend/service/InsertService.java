package shoppinglist.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shoppinglist.backend.dto.ItemDto;
import shoppinglist.backend.entity.CategoryEntity;
import shoppinglist.backend.entity.ItemEntity;
import shoppinglist.backend.entity.UnitEntity;
import shoppinglist.backend.repository.*;

@Service
@Slf4j
public class InsertService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RecipeItemRepository recipeItemRepository;
    @Autowired
    private ShoppingListRepository shoppingListRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private RecipeRepository recipeRepository;


    public void insertItem(ItemDto itemDto) {
        UnitEntity unitEntity = new UnitEntity();
        unitEntity.setUnitName(itemDto.getUnit());
        if (!unitRepository.existsByUnitName(itemDto.getUnit())) {
            unitRepository.save(unitEntity);
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryName(itemDto.getCategory());
        if (!categoryRepository.existsByCategoryName(itemDto.getCategory())) {
            categoryRepository.save(categoryEntity);
        }
        ItemEntity entity = new ItemEntity();
        entity.setUnit(itemDto.getUnit());
        entity.setItemName(itemDto.getName());
        entity.setCategory(itemDto.getCategory());
        ItemEntity test = itemRepository.getReferenceByItemNameAndUnit(itemDto.getName(), itemDto.getUnit());
        if (test == null) {
            itemRepository.save(entity);
        }
        long now = System.currentTimeMillis();
        test = itemRepository.getReferenceByItemNameAndUnit(Integer.toString(999999), itemDto.getUnit());
        log.info(Long.toString(System.currentTimeMillis() - now));
    }
}
