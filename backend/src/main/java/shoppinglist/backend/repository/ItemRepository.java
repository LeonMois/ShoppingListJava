package shoppinglist.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppinglist.backend.entity.ItemEntity;
import shoppinglist.backend.entity.UnitEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    ItemEntity findByItemNameIgnoreCaseAndUnit(String name, UnitEntity unit);

    ItemEntity findByItemNameIgnoreCase(String name);

}
