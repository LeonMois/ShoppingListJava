package shoppinglist.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "category_sequence", sequenceName = "hibernate_sequence", allocationSize = 100)
    private int id;

    @Column(unique = true)
    private String category;
}
