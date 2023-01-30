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
public class RecentReviewDto {
    private Long reviewId;

    private Long restaurantId;

    private List<ImgUrlDto> imgUrlDtoList;

    private String restaurantName;

    private String nickName;

    private int ratingStar;

    private String contents;
}
