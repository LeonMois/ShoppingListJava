package shoppinglist.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shoppinglist.backend.dto.ItemDto;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "item_sequence", sequenceName = "hibernate_sequence", allocationSize = 100)
    private int id;

    @Column(unique = true, name = "name")
    private String itemName;

    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @OneToOne(targetEntity = CategoryEntity.class)
    private CategoryEntity category;

    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = UnitEntity.class)
    private UnitEntity unit;

    public static ItemDto mapToDto(ItemEntity itemEntity) {
        ItemDto dto = new ItemDto();
        dto.setCategory(itemEntity.getCategory().getCategoryName());
        dto.setUnit(itemEntity.getUnit().getUnitName());
        dto.setName(itemEntity.getItemName());
        return dto;
    }
}
