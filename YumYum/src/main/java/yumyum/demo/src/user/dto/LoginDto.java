package yumyum.demo.src.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotBlank(message = "이메일을 입력해주세요.") //@NotBlank는 @NotNull을 포함
    @Size(max = 320, message = "잘못된 이메일 형식입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$", message = "잘못된 이메일 형식입니다.")
    @ApiModelProperty(example = "test123@gmail.com")
    private String email;

    @NotBlank(message = "비밀 번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 특수문자 포함 최소 8글자입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$", message = "비밀번호는 특수문자 포함 최소 8글자입니다.")
    @ApiModelProperty(example = "12345abcde!")
    private String password;
}
