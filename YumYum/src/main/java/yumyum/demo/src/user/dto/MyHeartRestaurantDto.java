package yumyum.demo.src.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyHeartRestaurantDto {
    private Long heartId;

    private Long restaurantId;
    private String imgUrl;
    private String restaurantName;
    private double averageStar;
    private String address;
    private int countHeart;
    private String restaurantType;

    private Long userId;

}
