package shoppinglist.backend.service;

import org.springframework.stereotype.Service;
import shoppinglist.backend.dto.ItemDto;
import shoppinglist.backend.entity.ItemEntity;
import shoppinglist.backend.entity.UnitEntity;
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
        itemRepository.findAll();
        return itemRepository.findAll().stream().map(itemEntity -> new ItemDto(itemEntity.getItemName(), itemEntity.getCategory().getCategoryName(), itemEntity.getUnit().getUnitName())).toList();
    }

    public ItemDto addItem(ItemDto itemDto) throws IOException {
        UnitEntity unit = unitService.getOrAddUnit(itemDto.getUnit());
        if (itemRepository.findByItemNameAndUnit(itemDto.getName(), unit) != null) {
            throw new IOException("Item already exists!");
        }
        ItemEntity item = new ItemEntity();
        item.setItemName(itemDto.getName());
        item.setCategory(categoryService.getOrAddCategory(itemDto.getCategory()));
        item.setUnit(unit);
        itemRepository.save(item);
        return itemDto;
    }

    public ItemDto deleteItem(ItemDto itemDto) throws IOException {
        UnitEntity unit = unitService.getOrAddUnit(itemDto.getUnit());
        if (itemRepository.findByItemNameAndUnit(itemDto.getName(), unit) != null) {
            throw new IOException("Item already exists!");
        }
        ItemEntity item = new ItemEntity();
        item.setItemName(itemDto.getName());
        item.setCategory(categoryService.getOrAddCategory(itemDto.getCategory()));
        item.setUnit(unit);
        itemRepository.delete(item);
        return itemDto;
    }

    public ItemDto updateItem(ItemDto oldItem, ItemDto newItem) throws IOException {
        ItemEntity oldEntry = itemRepository.findByItemNameAndUnit(oldItem.getName(), unitService.getUnit(oldItem.getUnit()));
        ItemEntity newEntry = itemRepository.findByItemNameAndUnit(oldItem.getName(), unitService.getUnit(oldItem.getUnit()));
        if (oldEntry == null || newEntry != null) {
            throw new IOException("Original item doesn't exist!");
        }
        oldEntry.setItemName(newItem.getName());
        oldEntry.setUnit(unitService.getOrAddUnit(newItem.getUnit()));
        oldEntry.setCategory(categoryService.getOrAddCategory(newItem.getCategory()));
        itemRepository.save(oldEntry);
        return newItem;
    }

    public ItemEntity getSingleItem(String itemName, String unit) throws IOException {
        ItemEntity item = itemRepository.findByItemNameAndUnit(itemName, unitService.getUnit(unit));
        if (item == null) {
            throw new IOException("Item doesn't exist!");
        }
        return item;
    }
}
