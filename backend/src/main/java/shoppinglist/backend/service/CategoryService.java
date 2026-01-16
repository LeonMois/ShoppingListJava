package shoppinglist.backend.service;

import org.springframework.stereotype.Service;
import shoppinglist.backend.dto.CategoryDto;
import shoppinglist.backend.entity.CategoryEntity;
import shoppinglist.backend.repository.CategoryRepository;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto addCategory(String category) throws IOException {
        CategoryEntity entity = categoryRepository.findEntityByCategoryNameIgnoreCase(category);
        if (entity != null) {
            // todo: write proper exception statuses
            throw new IOException("Category already exists!");
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryName(category);
        categoryRepository.save(categoryEntity);
        return new CategoryDto(category);
    }

    public List<CategoryDto> addCategoryList(List<CategoryDto> categories) throws IOException {
        for (CategoryDto category : categories) {
            try {
                addCategory(category.getCategoryName());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return categoryRepository.findAll().stream().map(entity -> new CategoryDto(entity.getCategoryName())).toList();
    }

    public CategoryDto deleteCategory(String category) throws IOException {
        CategoryEntity categoryEntity = categoryRepository.findEntityByCategoryNameIgnoreCase(category);
        if (categoryEntity == null) {
            throw new IOException("Category doesn't exist!");
        }
        categoryRepository.delete(categoryEntity);
        return new CategoryDto(categoryEntity.getCategoryName());
    }

    public CategoryDto updateCategory(String oldName, String newName) throws IOException {
        CategoryEntity oldEntity = categoryRepository.findEntityByCategoryNameIgnoreCase(oldName);
        if (oldEntity == null) {
            throw new IOException("Category doesn't exist!");
        }
        CategoryEntity newEntityInDatabase = categoryRepository.findEntityByCategoryNameIgnoreCase(newName);
        if (newEntityInDatabase != null && newEntityInDatabase.getId() != oldEntity.getId()) {
            throw new IOException("New Category already exists!!");
        }
        oldEntity.setCategoryName(newName);
        categoryRepository.save(oldEntity);
        return new CategoryDto(newName);
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryEntity -> new CategoryDto(categoryEntity.getCategoryName())).toList();
    }

    public CategoryEntity getOrAddCategory(String name) {
        CategoryEntity existingEntity = categoryRepository.findEntityByCategoryNameIgnoreCase(name);
        if (existingEntity != null) {
            return existingEntity;
        }
        CategoryEntity newEntity = new CategoryEntity();
        newEntity.setCategoryName(name);
        categoryRepository.save(newEntity);
        return categoryRepository.findEntityByCategoryNameIgnoreCase(name);

    }


}
