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
public class GetReviewDto {
    private Long reviewId;

    private List<ImgUrlDto> reviewImgUrl;

    private String nickName;

    private int ratingStar;

    private String contents;

    private String createdAt;
}
