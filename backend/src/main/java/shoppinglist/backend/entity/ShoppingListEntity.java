package shoppinglist.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shoppinglist.backend.dto.ShoppingListDto;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "shopping_list")
public class ShoppingListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "recipe_sequence", sequenceName = "hibernate_sequence", allocationSize = 100)
    private int id;

    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @OneToOne(targetEntity = ItemEntity.class)
    private ItemEntity item;

    private float quantity;

    // Sqlite doesn't support booleans, they are stored as 1 or 0
    private Integer deleted;


    public static ShoppingListDto mapToDto(ShoppingListEntity entity) {
        ShoppingListDto dto = new ShoppingListDto();
        dto.setItemName(entity.getItem().getItemName());
        dto.setUnitName(entity.getItem().getUnit().getUnitName());
        dto.setQuantity(entity.getQuantity());
        dto.setDeleted(entity.deleted == 1);
        return dto;
    }
}
