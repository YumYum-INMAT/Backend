package yumyum.demo.src.restaurant.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import yumyum.demo.config.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "restaurant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class RestaurantEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private String profileImgUrl;

    @Column(nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, columnDefinition = "decimal(18,10)")
    private Double latitude;

    @Column(nullable = false, columnDefinition = "decimal(18,10)")
    private Double longitude;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer countHeart;

    @Column(nullable = false, columnDefinition = "decimal(2,1) default 0")
    private Double averageStar;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer countReview;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer sumStar;

    @Column(nullable = false)
    private Integer averagePrice;

    @Column(nullable = false, columnDefinition = "int default 1")
    private Integer complexity; //1:여유, 2:보통, 3:복잡

    @Column(nullable = false, columnDefinition = "varchar(6) default 'FOOD'")
    private String restaurantType;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantImgEntity> restaurantImgEntities = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantMenuEntity> restaurantMenuEntities = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL)
    private List<ReviewEntity> reviewEntities = new ArrayList<>();

    public RestaurantEntity(String restaurantName, String profileImgUrl, String contactNumber, String address,
                            Double latitude, Double longitude, String restaurantType, List<RestaurantMenuEntity> restaurantMenuEntities) {
        this.restaurantName = restaurantName;
        this.profileImgUrl = profileImgUrl;
        this.contactNumber = contactNumber;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.restaurantType = restaurantType;

        for (RestaurantMenuEntity restaurantMenu : restaurantMenuEntities) {
            addRestaurantMenu(restaurantMenu);
        }
        updateAveragePrice();
    }

    /**
     * 비즈니스 로직 추가
     */
    public void addRestaurantMenu(RestaurantMenuEntity restaurantMenu) {
        restaurantMenuEntities.add(restaurantMenu);
        restaurantMenu.setRestaurant(this);
    }

    public void addReview(ReviewEntity reviewEntity) {
        //리뷰 추가
        reviewEntities.add(reviewEntity);
        addSumStar(reviewEntity.getRatingStar());
        reviewEntity.setRestaurant(this);

        //음식점 사진 추가
        if (!reviewEntity.getReviewImgEntities().isEmpty()) {
            for (ReviewImgEntity reviewImgEntity : reviewEntity.getReviewImgEntities()) {
                RestaurantImgEntity restaurantImgEntity = new RestaurantImgEntity();
                restaurantImgEntity.setImgUrl(reviewImgEntity.getImgUrl());
                restaurantImgEntities.add(restaurantImgEntity);
                restaurantImgEntity.setRestaurant(this);
            }
        }
    }

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

    //복잡도 수정
    public void setComplexity(int level) {
        this.complexity = level;
    }



}
