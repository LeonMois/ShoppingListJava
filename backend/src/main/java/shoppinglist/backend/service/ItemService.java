package shoppinglist.backend.service;

import org.springframework.stereotype.Service;
import shoppinglist.backend.dto.ItemDto;
import shoppinglist.backend.entity.ItemEntity;
import shoppinglist.backend.repository.ItemRepository;

import java.io.IOException;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UnitService unitService;
    private final CategoryService categoryService;

    public ItemService(ItemRepository itemRepository, UnitService unitService, CategoryService categoryService) {
        this.itemRepository = itemRepository;
        this.unitService = unitService;
        this.categoryService = categoryService;
    }

    public List<ItemDto> getAllItems() {
        return itemRepository.findAll().stream().map(itemEntity -> new ItemDto(itemEntity.getItemName(), itemEntity.getCategory().getCategoryName(), itemEntity.getUnit().getUnitName())).toList();
    }

    public ItemDto addItem(ItemDto itemDto) throws IOException {
        if (itemRepository.findByItemNameAndUnit(itemDto.getName(), itemDto.getUnit()) != null) {
            throw new IOException("Item already exists!");
        }
        ItemEntity entity = new ItemEntity();
        entity.setItemName(itemDto.getName());
        entity.setCategory(categoryService.getOrAddCategory(itemDto.getCategory()));
        entity.setUnit(unitService.getOrAddUnit(itemDto.getUnit()));
        return itemDto;
    }

    public ItemDto delete(ItemDto itemDto) throws IOException {
        if (itemRepository.findByItemNameAndUnit(itemDto.getName(), itemDto.getUnit()) != null) {
            throw new IOException("Item already exists!");
        }
        ItemEntity entity = new ItemEntity();
        entity.setItemName(itemDto.getName());
        entity.setCategory(categoryService.getOrAddCategory(itemDto.getCategory()));
        entity.setUnit(unitService.getOrAddUnit(itemDto.getUnit()));
        return itemDto;
    }
}
