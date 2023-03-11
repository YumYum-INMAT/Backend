package yumyum.demo.src.user.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class EmailDto {
    @NotBlank(message = "이메일을 입력해주세요.") //@NotBlank는 @NotNull을 포함
    @Size(max = 320, message = "잘못된 이메일 형식입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$", message = "잘못된 이메일 형식입니다.")
    @ApiModelProperty(example = "test123@gmail.com")
    private String email;
}
