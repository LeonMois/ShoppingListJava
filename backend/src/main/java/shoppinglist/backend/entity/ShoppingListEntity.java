package shoppinglist.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
    private int shoppingListId;

    @JoinColumn(name = "recipe_item_id", insertable = false, updatable = false)
    @OneToOne(targetEntity = RecipeItemEntity.class)
    private RecipeItemEntity recipeItem;

    private float quantity;

}
