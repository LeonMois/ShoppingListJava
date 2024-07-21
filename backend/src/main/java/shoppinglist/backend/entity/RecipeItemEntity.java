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

    @ManyToOne(targetEntity = RecipeEntity.class)
    private RecipeEntity recipe;

    @Column(name = "recipe_name")
    private String recipeName;

    @ManyToOne(targetEntity = ItemEntity.class)
    private ItemEntity item;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "category")
    private String category;

    private int amount;
}
