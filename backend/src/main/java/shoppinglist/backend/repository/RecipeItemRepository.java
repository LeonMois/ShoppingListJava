package shoppinglist.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppinglist.backend.entity.RecipeItemEntity;

public interface RecipeItemRepository extends JpaRepository<RecipeItemEntity, Integer> {

}
