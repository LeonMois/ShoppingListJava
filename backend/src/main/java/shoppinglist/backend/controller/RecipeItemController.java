package shoppinglist.backend.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shoppinglist.backend.dto.RecipeItemDto;
import shoppinglist.backend.service.RecipeItemService;

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

  @PostMapping(path = "/item/add")
  public RecipeItemDto addRecipeItem(@RequestBody RecipeItemDto recipeItemDto) throws IOException {
    return recipeItemService.addRecipeItem(recipeItemDto);
  }

  @DeleteMapping(path = "/item/delete")
  public RecipeItemDto deleteRecipeItem(@RequestBody RecipeItemDto recipeItemDto) throws IOException {
    return recipeItemService.deleteRecipeItem(recipeItemDto);
  }

  @PutMapping(path = "/item/update")
  public RecipeItemDto updateRecipeItem(@RequestBody RecipeItemDto oldRecipeItem, @RequestBody RecipeItemDto newRecipeItem) throws IOException {
    return recipeItemService.updateRecipeItem(oldRecipeItem, newRecipeItem);
  }
}
