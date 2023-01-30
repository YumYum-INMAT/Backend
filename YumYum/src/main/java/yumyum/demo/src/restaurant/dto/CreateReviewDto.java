package yumyum.demo.src.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewDto {
    @NotNull(message = "평점을 입력해주세요.")
    @Range(min = 1, max = 5, message = "평점은 1이상 5이하의 정수입니다.")
    @ApiModelProperty(example = "5")
    private Integer ratingStar;

    @Size(max = 5, message = "사진은 최대 5장까지 가능합니다.")
    private List<ImgUrlDto> imgUrlDtoList;

    @NotBlank(message = "리뷰 내용을 입력해주세요")
    @Size(max = 200, message = "리뷰 내용은 최대 200자까지 입력 가능합니다.")
    @ApiModelProperty(example = "맛있어요 :)")
    private String contents;
}
