package shoppinglist.backend.controller;

import java.io.IOException;
import org.springframework.web.bind.annotation.*;
import shoppinglist.backend.dto.RecipeDto;

import java.util.List;
import shoppinglist.backend.service.RecipeService;

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

    @PostMapping(path = "/recipe/add")
    public RecipeDto addRecipe(@RequestBody RecipeDto recipeDto) throws IOException {
        return recipeService.addRecipe(recipeDto);
    }

    @DeleteMapping(path = "/recipe/delete")
    public RecipeDto deleteRecipe(@RequestBody RecipeDto recipeDto) throws IOException {

        return recipeService.deleteRecipe(recipeDto);
    }

    @PutMapping(path = "/recipe/{oldRecipe}/{newRecipe}")
    public RecipeDto getRecipeItems(@RequestBody RecipeDto oldRecipe, @RequestBody RecipeDto newRecipe) throws IOException {
        return recipeService.updateRecipe(oldRecipe, newRecipe);
    }


}
