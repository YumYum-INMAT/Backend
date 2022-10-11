package yumyum.demo.src.restaurant.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "restaurant")
@NoArgsConstructor
@DynamicInsert
public class RestaurantEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false, columnDefinition = "default 0")
    private Integer countHeart;

    @Column(nullable = false, columnDefinition = "default 0")
    private Double averageStar;

    @Column(nullable = false, columnDefinition = "default 0")
    private Integer countReview;

    @Column(nullable = false)
    private Double averagePrice;

    @Column(nullable = false, columnDefinition = "default 1")
    private Integer complexity;

    @Column(nullable = false, columnDefinition = "varchar(5) default 'FOOD'")
    private String type;

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantMenuEntity> restaurantMenuEntities = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<ReviewEntity> reviewEntities = new ArrayList<>();

}
