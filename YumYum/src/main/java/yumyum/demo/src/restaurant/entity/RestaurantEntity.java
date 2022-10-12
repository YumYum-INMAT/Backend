package yumyum.demo.src.restaurant.entity;

import lombok.Builder;
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

    @Column(nullable = false, columnDefinition = "decimal(18,10)")
    private Double latitude;

    @Column(nullable = false, columnDefinition = "decimal(18,10)")
    private Double longitude;

    @Column(nullable = false, columnDefinition = "default 0")
    private Integer countHeart;

    @Column(nullable = false, columnDefinition = "decimal(2,1) default 0")
    private Double averageStar;

    @Column(nullable = false, columnDefinition = "default 0")
    private Integer countReview;

    @Column(nullable = false, columnDefinition = "default 0")
    private Integer sumStar;

    @Column(nullable = false)
    private Integer averagePrice;

    @Column(nullable = false, columnDefinition = "default 1")
    private Integer complexity;

    @Column(nullable = false, columnDefinition = "varchar(5) default 'FOOD'")
    private String type;

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantMenuEntity> restaurantMenuEntities = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<ReviewEntity> reviewEntities = new ArrayList<>();

    @Builder
    public RestaurantEntity(Long id, String restaurantName, String imgUrl, String contactNumber,
                            String address, Double latitude, Double longitude, String type) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.imgUrl = imgUrl;
        this.contactNumber = contactNumber;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    /**
     * 비즈니스 로직 추가
     */

    //하트 찜 개수 증가
    public void increaseCountHeart() {
        this.countHeart++;
    }

    //하트 찜 개수 감소
    public void decreaseCountHeart() {
        this.countHeart--;
    }

    //별점 합 더하기 -> 리뷰 개수 증가 -> 평균 별점 수정
    public void addSumStar(int ratingStar) {
        this.sumStar += ratingStar;
        this.countReview++;
        this.averageStar = sumStar.doubleValue() / countReview.doubleValue();
    }

    //별점 합 빼기 -> 리뷰 개수 감소 -> 평균 별점 수정
    public void subtractSumStar(int ratingStar) {
        this.sumStar -= ratingStar;
        this.countReview--;
        this.averageStar = sumStar.doubleValue() / countReview.doubleValue();
    }

    //평균 가격 업데이트
    public void updateAveragePrice() {
        int sumPrice = 0;
        int size = 0;
        for(RestaurantMenuEntity restaurantMenu : restaurantMenuEntities) {
            sumPrice += restaurantMenu.getPrice();
            size++;
        }
        this.averagePrice = sumPrice / size;
    }




}
