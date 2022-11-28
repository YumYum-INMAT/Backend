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
public class GetRestaurantsDto {
    private List<BannerDto> bannerList;

    private List<TodayRecommendDto> todayRecommendList;

    private List<RecentReviewDto> recentReviewList;

    private List<RestaurantDto> restaurantList;
}
