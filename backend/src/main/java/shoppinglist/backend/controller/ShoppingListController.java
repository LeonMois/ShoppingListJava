package shoppinglist.backend.controller;

import org.springframework.web.bind.annotation.*;
import shoppinglist.backend.dto.ItemDto;
import shoppinglist.backend.dto.RecipeDto;
import shoppinglist.backend.dto.ShoppingListDto;

import java.util.List;

@RestController
@RequestMapping("/shoppingList")
public class ShoppingListController {

    @GetMapping(path = "/")
    public ShoppingListDto getShoppingList() {

        return new ShoppingListDto();
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
