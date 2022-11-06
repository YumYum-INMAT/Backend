package yumyum.demo.src.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsernameDto {
    @NotBlank(message = "아이디을 입력해주세요.") //@NotBlank는 @NotNull을 포함
    @Size(min = 3,max = 10, message = "아이디는 최소 3자, 최대 10자입니다.")
    @Pattern(regexp = "[a-zA-Z0-9]{3,10}", message = "잘못된 아이디 형식입니다.")
    @ApiModelProperty(example = "test123")
    private String username;
}
