package shoppinglist.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppinglist.backend.entity.RecipeEntity;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer> {
    RecipeEntity findByRecipeNameIgnoreCase(String recipeName);
}
