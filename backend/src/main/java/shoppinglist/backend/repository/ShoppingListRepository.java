package shoppinglist.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppinglist.backend.entity.ShoppingListEntity;

public interface ShoppingListRepository extends JpaRepository<ShoppingListEntity, Integer> {

}
