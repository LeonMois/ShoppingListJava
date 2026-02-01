package shoppinglist.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shoppinglist.backend.dto.RecipeDto;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "recipe")
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, name = "name")
    private String recipeName;

    private int servings;

    public static RecipeDto mapToDto(RecipeEntity entity) {
        RecipeDto dto = new RecipeDto();
        dto.setName(entity.getRecipeName());
        dto.setServings(entity.getServings());
        return dto;
    }
}


