package shoppinglist.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppinglist.backend.entity.Category;

import java.util.List;

public interface TypeRepository extends JpaRepository<Category,Integer> {

}
