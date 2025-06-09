package shoppinglist.backend.controller;

import org.springframework.web.bind.annotation.*;
import shoppinglist.backend.dto.RecipeDto;
import shoppinglist.backend.dto.ShoppingListDto;
import shoppinglist.backend.service.ShoppingListService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/shopping-list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public List<ShoppingListDto> getShoppingList(@RequestParam(required = false) String sortOrder) {
        return shoppingListService.getAll(sortOrder);
    }

    @PostMapping(path = "/add/items")
    public List<ShoppingListDto> addItemToShoppingList(@RequestBody List<ShoppingListDto> items) throws IOException {
        return shoppingListService.addItems(items);
    }


    @PutMapping(path = "/toggle/items")
    public List<ShoppingListDto> setItemsToDeleted(@RequestBody List<ShoppingListDto> items) throws IOException {

        return shoppingListService.setItemsToDeleted(items);
    }

    @DeleteMapping(path = "/delete")
    public List<ShoppingListDto> setItemsToDeleted() throws IOException {

        return shoppingListService.removeItems();
    }


    @PostMapping(path = "/add/recipes")
    public List<ShoppingListDto> addRecipeToShoppingList(@RequestBody List<RecipeDto> recipes) throws IOException {

        return shoppingListService.addRecipes(recipes);
    }

}
