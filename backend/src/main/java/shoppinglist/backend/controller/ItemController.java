package shoppinglist.backend.controller;

import org.springframework.web.bind.annotation.*;
import shoppinglist.backend.dto.ItemDto;
import shoppinglist.backend.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping()
    public List<ItemDto> getItems() {

        return List.of();
    }

    @PostMapping(path = "/item/{itemDto}")
    public ItemDto insertItem(@PathVariable ItemDto itemDto) {

        return itemDto;
    }

    @DeleteMapping(path = "/item/{itemDto}")
    public ItemDto deleteItem(@PathVariable ItemDto itemDto) {

        return itemDto;
    }


    @PutMapping(path = "/item/{itemDto}")
    public ItemDto updateItem(@PathVariable ItemDto itemDto) {

        return itemDto;
    }

}
