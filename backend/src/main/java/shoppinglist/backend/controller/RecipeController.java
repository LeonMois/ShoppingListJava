package shoppinglist.backend.controller;

import org.springframework.web.bind.annotation.*;
import shoppinglist.backend.dto.RecipeDto;
import shoppinglist.backend.entity.RecipeEntity;
import shoppinglist.backend.service.RecipeService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeDto> getRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{recipe}")
    public RecipeDto getOneRecipe(@PathVariable String recipe) throws IOException {
        return RecipeEntity.mapToDto(recipeService.getSingleRecipe(recipe));
    }

    @PostMapping(path = "/add")
    public RecipeDto addRecipe(@RequestBody RecipeDto recipeDto) throws IOException {
        return recipeService.addRecipe(recipeDto);
    }

    @DeleteMapping(path = "/delete")
    public RecipeDto deleteRecipe(@RequestBody RecipeDto recipeDto) throws IOException {

        return recipeService.deleteRecipe(recipeDto);
    }

    @PutMapping(path = "/update")
    public RecipeDto getRecipeItems(@RequestBody List<RecipeDto> recipes) throws IOException {
        return recipeService.updateRecipe(recipes.getFirst(), recipes.getLast());
    }


}
