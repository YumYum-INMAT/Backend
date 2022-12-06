package yumyum.demo.src.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetReviewDto {
    private Long reviewId;

    private String reviewImgUrl;

    private String nickName;

    private int ratingStar;

    private String contents;

    private String createdAt;
}
