package yumyum.demo.src.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecentReviewDto {
    private Long reviewId;

    private Long restaurantId;

    private String imgUrl;

    private String restaurantName;

    private String nickName;

    private int ratingStar;

    private String contents;
}
