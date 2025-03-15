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

    @JoinColumn(name = "id", referencedColumnName = "id")
    @OneToOne(targetEntity = ItemEntity.class)
    private ItemEntity recipeItem;

    private float quantity;


    public static ShoppingListDto mapToDto(ShoppingListEntity entity){
        ShoppingListDto dto = new ShoppingListDto();
        dto.setRecipeItem(entity.getRecipeItem());
        dto.setQuantity(entity.getQuantity());
        return dto;
    }
}
