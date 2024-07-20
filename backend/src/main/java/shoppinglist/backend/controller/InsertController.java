package shoppinglist.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shoppinglist.backend.dto.CategoriesDto;
import shoppinglist.backend.entity.CategoryEntity;
import shoppinglist.backend.repository.TypeRepository;

@RestController
public class InsertController {

    private final TypeRepository typeRepository;

    public InsertController(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }


    @GetMapping(path = "/insert")
    public void insertCategory(@RequestParam String category){
        CategoryEntity category1 = new CategoryEntity();
        category1.setCategory(category);
        typeRepository.save(category1);
    }

    @GetMapping(path = "/all")
    public CategoriesDto getAllCategories(){
        return new CategoriesDto(typeRepository.findAll().stream().map(CategoryEntity::getCategory).toList());
    }

}
