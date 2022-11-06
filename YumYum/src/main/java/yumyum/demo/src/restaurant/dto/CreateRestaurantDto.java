package yumyum.demo.src.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRestaurantDto {

    @NotBlank(message = "음식점 이름을 입력해주세요.")
    @Size(min = 1, max = 20, message = "음식점 이름은 최소 1자, 최대 20자까지 입니다.")
    @ApiModelProperty(example = "닭살부부")
    private String restaurantName;

    @NotNull(message = "URL을 입력해주세요")
    @Pattern(regexp = "^((http|https)://)?(www.)?([a-zA-Z0-9]+)\\.[a-z]+([a-zA-Z0-9.?#]+)?", message = "올바른 URL이 아닙니다.")
    @ApiModelProperty(example = "www.test.test")
    private String imgUrl;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "잘못된 휴대폰 번호입니다.")
    @ApiModelProperty(example = "010-1234-5678")
    private String contactNumber;

    @NotBlank(message = "주소를 입력해주세요.")
    @Size(min = 1, max = 30, message = "주소는 최대 30자까지 입니다.")
    @ApiModelProperty(example = "미추홀구 1번길 1")
    private String address;

    @NotNull(message = "위도를 입력해주세요")
    @ApiModelProperty(example = "36.0000")
    private Double latitude;

    @NotNull(message = "경도를 입력해주세요")
    @ApiModelProperty(example = "36.0000")
    private Double longitude;

    @NotBlank(message = "음식점 종류를 입력해주세요")
    @ApiModelProperty(example = "FOOD")
    private String restaurantType;

    private List<RestaurantMenuDto> restaurantMenuList;

}
