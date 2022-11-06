package yumyum.demo.src.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantMenuDto {
    @NotNull(message = "카테고리 ID 값을 입력하세요.")
    @ApiModelProperty(example = "1")
    private Long categoryId;

    @NotBlank(message = "메뉴 이름을 입력해주세요.")
    @Size(min = 1, max = 20, message = "메뉴 이름은 최소 1자, 최대 20자까지 입니다.")
    @ApiModelProperty(example = "탕수육")
    private String menuName;

    @NotNull(message = "메뉴 가격을 입력하세요.")
    @Range(min = 0, message = "가격은 0원 이상입니다.")
    @ApiModelProperty(example = "10000")
    private Integer price;
}
