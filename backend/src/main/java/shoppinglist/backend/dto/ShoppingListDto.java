package shoppinglist.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shoppinglist.backend.entity.RecipeItemEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingListDto {

    private RecipeItemEntity recipeItem;

    private int servings;

}
