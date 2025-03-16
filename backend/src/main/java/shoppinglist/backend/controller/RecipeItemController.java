package shoppinglist.backend.controller;

import org.springframework.web.bind.annotation.*;
import shoppinglist.backend.dto.RecipeItemDto;
import shoppinglist.backend.entity.RecipeItemEntity;
import shoppinglist.backend.service.RecipeItemService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/recipe-items")
public class RecipeItemController {

    private final RecipeItemService recipeItemService;

    public RecipeItemController(RecipeItemService recipeItemService) {
        this.recipeItemService = recipeItemService;
    }

    @GetMapping
    public List<RecipeItemDto> getRecipeItems() {
        return recipeItemService.getAllRecipeItems();
    }

    @GetMapping("/{recipeName}")
    public List<RecipeItemDto> getRecipeItems(@PathVariable String recipeName) throws IOException {
        return recipeItemService.getItemsForRecipe(recipeName).stream().map(RecipeItemEntity::mapToDto).toList();
    }

    @PostMapping(path = "/add")
    public List<RecipeItemDto> addItemsToRecipe(@RequestBody List<RecipeItemDto> items) throws IOException {
        return recipeItemService.addItemsToRecipe(items);
    }

    @DeleteMapping(path = "/delete")
    public List<RecipeItemDto> deleteRecipeItem(@RequestBody List<RecipeItemDto> recipeItemDto) throws IOException {
        return recipeItemService.deleteRecipeItem(recipeItemDto);
    }

    @PutMapping(path = "/update")
    public List<RecipeItemDto> updateRecipeItem(@RequestBody List<RecipeItemDto> recipeItems) throws IOException {
        return recipeItemService.updateRecipeItem(recipeItems);
    }
}
