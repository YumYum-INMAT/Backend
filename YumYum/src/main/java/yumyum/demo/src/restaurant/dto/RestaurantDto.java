package yumyum.demo.src.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantDto {
    private Long restaurantId;

    private String profileImgUrl;

    private String restaurantName;

    private String address;

    private Double averageStar;

    private Integer countReview;

    private Integer averagePrice;

    private Integer complexity; //1:여유, 2:보통, 3:복잡

    private String restaurantType;

    private boolean userHeart; //유저 하트 여부
}
