package shoppinglist.backend.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "category_sequence", sequenceName = "hibernate_sequence", allocationSize = 100)
    private int id;

    @Column(unique = true)
    private String category;
}
