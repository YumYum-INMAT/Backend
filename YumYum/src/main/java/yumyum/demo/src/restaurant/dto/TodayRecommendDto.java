package yumyum.demo.src.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodayRecommendDto {
    private Long recommendId;

    private String imgUrl;

    private String foodName;
}
