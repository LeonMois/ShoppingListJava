package shoppinglist.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppinglist.backend.entity.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    ItemEntity getReferenceByItemNameAndUnit(String name, String unit);

}
