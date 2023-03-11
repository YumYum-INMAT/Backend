package yumyum.demo.src.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class imgUrlDto {

    @ApiModelProperty(example = "www.test.img")
    @Size(min = 0, message = "null 값 또는 URL을 입력해주세요")
    @Pattern(regexp = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#()?&//=]*)" ,message = "올바른 URL이 아닙니다.")
    private String imgUrl;

}
