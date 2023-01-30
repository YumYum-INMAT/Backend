package yumyum.demo.src.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImgUrlDto {
    @Pattern(regexp = "^((http|https)://)?(www.)?([a-zA-Z0-9]+)\\.[a-z]+([a-zA-Z0-9.?#]+)?", message = "올바른 URL이 아닙니다.")
    @ApiModelProperty(example = "www.test.test")
    private String imgUrl;
}
