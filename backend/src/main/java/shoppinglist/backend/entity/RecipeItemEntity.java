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
    private int id;

    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    @OneToOne(targetEntity = RecipeEntity.class)
    private RecipeEntity recipe;

    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = ItemEntity.class)
    private ItemEntity item;

    private float quantity;
}
