package shoppinglist.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
