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

    @Column(unique = true, name = "item_name")
    private String itemName;

    @JoinColumn(name = "category")
    @ManyToOne(targetEntity = CategoryEntity.class)
    private CategoryEntity category;

    @JoinColumn(name = "unit")
    @ManyToOne(targetEntity = UnitEntity.class)
    private UnitEntity unit;

}
