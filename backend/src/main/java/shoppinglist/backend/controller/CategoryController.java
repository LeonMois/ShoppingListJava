package shoppinglist.backend.controller;

import org.springframework.web.bind.annotation.*;
import shoppinglist.backend.dto.CategoryDto;
import shoppinglist.backend.service.CategoryService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> getCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping(path = "/{categoryDto}")
    public CategoryDto addCategory(@PathVariable String categoryDto) throws IOException {
        return categoryService.addCategory(categoryDto);
    }

    @PutMapping(path = "/{oldName}/{newName}")
    public CategoryDto updateCategory(@PathVariable("oldName") String oldName, @PathVariable("newName") String newName) throws IOException {
        return categoryService.updateCategory(oldName, newName);
    }

    @DeleteMapping(path = "/{categoryDto}")
    public CategoryDto deleteCategory(@PathVariable String categoryDto) throws IOException {
        return categoryService.deleteCategory(categoryDto);
    }
}
