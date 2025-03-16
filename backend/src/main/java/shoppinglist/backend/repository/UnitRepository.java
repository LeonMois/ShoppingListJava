package shoppinglist.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppinglist.backend.entity.UnitEntity;

public interface UnitRepository extends JpaRepository<UnitEntity, Integer> {

    UnitEntity findEntityByUnitNameIgnoreCase(String name);
}
