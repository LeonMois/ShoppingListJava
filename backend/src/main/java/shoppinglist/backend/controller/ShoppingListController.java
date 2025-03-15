package shoppinglist.backend.controller;

import org.springframework.web.bind.annotation.*;
import shoppinglist.backend.dto.ItemDto;
import shoppinglist.backend.dto.RecipeDto;
import shoppinglist.backend.dto.ShoppingListDto;
import shoppinglist.backend.service.ShoppingListService;

import java.util.List;

@RestController
@RequestMapping("/shopping-list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService){
        this.shoppingListService = shoppingListService;
    }
    @GetMapping
    public List<ShoppingListDto> getShoppingList() {

        return shoppingListService.getAll();
    }

    @PostMapping(path = "/item/{itemDto}")
    public ItemDto addItemToShoppingList(@PathVariable ItemDto itemDto) {

        return itemDto;
    }


    @DeleteMapping(path = "/item/{itemDto}")
    public ItemDto deleteItemFromShoppingList(@PathVariable ItemDto itemDto) {

        return itemDto;
    }

    @PostMapping(path = "/recipe/{recipeDto}")
    public List<ItemDto> addRecipeToShoppingList(@PathVariable RecipeDto recipeDto) {

        return List.of();
    }

    @DeleteMapping(path = "/recipe/{recipeDto}")
    public List<ItemDto> deleteRecipeFromShoppingList(@PathVariable RecipeDto recipeDto) {

        return List.of();
    }
}
