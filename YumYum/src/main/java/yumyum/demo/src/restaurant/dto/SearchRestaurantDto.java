package yumyum.demo.src.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRestaurantDto {
    private Long restaurantId;

    private String profileImgUrl;

    private String restaurantName;

    private String address;

    private Double averageStar;

    private Integer countReview;

    private Integer averagePrice;

}
