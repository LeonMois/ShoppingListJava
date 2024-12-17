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
@Table(name = "recipe_item")
public class RecipeItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "recipe_sequence", sequenceName = "hibernate_sequence", allocationSize = 100)
    private int recipeItemId;

    @JoinColumn(name = "recipe_name", referencedColumnName = "recipe_name")
    @ManyToOne(targetEntity = RecipeEntity.class)
    private RecipeEntity recipe;

    @JoinColumn(name = "item_name", referencedColumnName = "item_name")
    @ManyToOne(targetEntity = ItemEntity.class)
    private ItemEntity item;

    private float quantity;
}
