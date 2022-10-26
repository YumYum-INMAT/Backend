package yumyum.demo.src.restaurant.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "restaurantMenu")
@NoArgsConstructor
@DynamicInsert
public class RestaurantMenuEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(nullable = false, columnDefinition = "varchar(30)")
    private String menuName;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer price;

}
