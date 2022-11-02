package yumyum.demo.src.restaurant.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
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

    @Builder
    public RestaurantMenuEntity(Long id, RestaurantEntity restaurant,
                                CategoryEntity category, String menuName, Integer price) {
        this.id = id;
        this.restaurant = restaurant;
        this.category = category;
        this.menuName = menuName;
        this.price = price;
    }
}
