package shoppinglist.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shoppinglist.backend.dto.RecipeItemDto;

import java.util.Objects;


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

    public static RecipeItemDto mapToDto(RecipeItemEntity entity) {
        RecipeItemDto dto = new RecipeItemDto();
        dto.setCategory(entity.getItem().getCategory().getCategoryName());
        dto.setQuantity(entity.getQuantity());
        dto.setRecipeName(entity.getRecipe().getRecipeName());
        dto.setUnit(entity.getItem().getUnit().getUnitName());
        dto.setItemName(entity.getItem().getItemName());
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeItemEntity that = (RecipeItemEntity) o;
        return Objects.equals(recipe.getRecipeName(), that.recipe.getRecipeName()) && Objects.equals(item.getItemName(), that.item.getItemName());
    }
}
