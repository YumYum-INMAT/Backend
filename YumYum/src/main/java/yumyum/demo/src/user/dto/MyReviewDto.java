package yumyum.demo.src.user.dto;


import java.util.List;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyReviewDto {
    private Long reviewId;
    private String contents;
    private List<String> imgUrlList;
    private int ratingStar;
    private Long restaurantId;
    private Long userId;

    private String restaurantName;

    private String createdAt;

}
