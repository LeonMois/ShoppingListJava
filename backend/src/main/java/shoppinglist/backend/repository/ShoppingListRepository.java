package shoppinglist.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppinglist.backend.entity.ItemEntity;
import shoppinglist.backend.entity.ShoppingListEntity;

import java.util.List;

public interface ShoppingListRepository extends JpaRepository<ShoppingListEntity, Integer> {

    List<ShoppingListEntity> findByItem(ItemEntity item);

    List<ShoppingListEntity> findByDeleted(Integer i);

}
