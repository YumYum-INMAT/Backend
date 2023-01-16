package yumyum.demo.src.user.dto;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyReviewDto {
    private Long reviewId;
    private String contents;
    private String imgUrl;
    private int ratingStar;
    private Long restaurantId;
    private Long userId;

    private String restaurantName;

    private String createdAt;

}
