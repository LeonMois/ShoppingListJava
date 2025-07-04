package shoppinglist.backend.controller;

import org.springframework.web.bind.annotation.*;
import shoppinglist.backend.dto.ItemDto;
import shoppinglist.backend.service.ItemService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping()
    public List<ItemDto> getItems() {
        return itemService.getAllItems();
    }

    @PostMapping(path = "/item/add")
    public ItemDto insertItem(@RequestBody ItemDto itemDto) throws IOException {

        return itemService.addItem(itemDto);
    }

    @DeleteMapping(path = "/item/delete")
    public ItemDto deleteItem(@RequestBody ItemDto itemDto) throws IOException {

        return itemService.deleteItem(itemDto);
    }


    @PutMapping(path = "/item/update")
    public ItemDto updateItem(@RequestBody List<ItemDto> items) throws IOException {

        return itemService.updateItem(items.getFirst(), items.getLast());
    }

    @PostMapping(path = "/item/addList")
    public List<ItemDto> insertItem(@RequestBody List<ItemDto> itemDto) throws IOException {

        return itemService.addItemList(itemDto);
    }

}
