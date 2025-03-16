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

    @PostMapping(path = "/add")
    public CategoryDto addCategory(@RequestBody CategoryDto categoryDto) throws IOException {
        return categoryService.addCategory(categoryDto.getCategoryName());
    }

    @PutMapping(path = "/update")
    public CategoryDto updateCategory(@RequestBody List<CategoryDto> categories) throws IOException {
        return categoryService.updateCategory(categories.getFirst().getCategoryName(), categories.getLast().getCategoryName());
    }

    @DeleteMapping(path = "/delete")
    public CategoryDto deleteCategory(@RequestBody CategoryDto categoryDto) throws IOException {
        return categoryService.deleteCategory(categoryDto.getCategoryName());
    }
}
