package yumyum.demo.src.restaurant.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetRestaurantDetailDto {
    private Long restaurantId;

    private String profileImgUrl; //음식점 프로필 사진

    private String restaurantName;

    private List<String> restaurantImgList; //음식점 사진 리스트

    private List<RestaurantMenuDto> menuList; //음식점 메뉴 리스트

    private String address; //주소

    private Double latitude; // 위도

    private Double longitude; // 경도

    private boolean userHeart; //유저 하트 여부

    private String contactNumber; //연락처

    private Double averageStar; //평점

    private Integer countReview;

    private Integer countHeart;

    private Integer averagePrice; //평균 가격

    private Integer complexity; //1:여유, 2:보통, 3:복잡

    private String restaurantType;

    private List<GetReviewDto> reviewList; //음식점 리뷰 리스트
}
