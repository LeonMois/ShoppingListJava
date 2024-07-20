package shoppinglist.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppinglist.backend.entity.CategoryEntity;

public interface TypeRepository extends JpaRepository<CategoryEntity,Integer> {

}
