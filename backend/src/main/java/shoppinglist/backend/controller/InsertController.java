package shoppinglist.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shoppinglist.backend.dto.ItemDto;
import shoppinglist.backend.service.InsertService;

@RestController
public class InsertController {

    private final InsertService insertService;

    public InsertController(InsertService insertService) {
        this.insertService = insertService;
    }

    @GetMapping(path = "/insert")
    public void insertCategory(@RequestParam String name, @RequestParam String category, @RequestParam String unit) {

        ItemDto itemDto = new ItemDto(name, category, unit);
        insertService.insertItem(itemDto);
    }

}
