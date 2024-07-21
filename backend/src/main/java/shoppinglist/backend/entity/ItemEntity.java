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
@Table(name = "item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "item_sequence", sequenceName = "hibernate_sequence", allocationSize = 100)
    private int itemId;

    private String itemName;

    @JoinColumn(name = "category", insertable = false, updatable = false)
    @OneToOne(targetEntity = CategoryEntity.class)
    private CategoryEntity categoryEntity;

    private String category;

    @JoinColumn(name = "unit", insertable = false, updatable = false)
    @OneToOne(targetEntity = UnitEntity.class)
    private UnitEntity unitEntity;

    private String unit;
}
