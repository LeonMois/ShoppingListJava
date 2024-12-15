package shoppinglist.backend.controller;

import org.springframework.web.bind.annotation.*;
import shoppinglist.backend.dto.RecipeDto;
import shoppinglist.backend.dto.RecipeItemDto;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @GetMapping(path = "/")
    public List<RecipeDto> getRecipes() {

        return List.of();
    }

    @PostMapping(path = "/{recipe}")
    public RecipeDto insertOrUpdateRecipe(@PathVariable RecipeDto recipeDto) {

        return recipeDto;
    }

    @DeleteMapping(path = "/{recipe}")
    public RecipeDto deleteRecipe(@PathVariable RecipeDto recipeDto) {

        return recipeDto;
    }

    @GetMapping(path = "/item/{recipeDto}")
    public List<RecipeItemDto> getRecipeItems(@PathVariable RecipeDto recipeDto) {

        return List.of();
    }

    @PostMapping(path = "/item/{recipe}")
    public RecipeItemDto insertOrUpdateRecipeItem(@PathVariable RecipeItemDto recipeItemDto) {

        return recipeItemDto;
    }

    @DeleteMapping(path = "/item/{recipe}")
    public RecipeItemDto deleteRecipeItem(@PathVariable RecipeItemDto recipeItemDto) {

        return recipeItemDto;
    }

}
