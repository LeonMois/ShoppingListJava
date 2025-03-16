package shoppinglist.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeItemDto {
    private String recipeName;

    private String itemName;

    private String unit;

    private String category;

    private float quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeItemDto that = (RecipeItemDto) o;
        return Objects.equals(recipeName, that.recipeName) && Objects.equals(itemName, that.itemName) && Objects.equals(unit, that.unit);
    }

}
