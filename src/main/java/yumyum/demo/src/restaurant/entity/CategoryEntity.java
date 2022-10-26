package yumyum.demo.src.restaurant.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "category")
@NoArgsConstructor
@DynamicInsert
public class CategoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(10)")
    private String categoryName;


}
