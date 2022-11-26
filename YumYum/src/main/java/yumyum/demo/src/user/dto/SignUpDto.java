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
public class SignUpDto {

    @NotBlank(message = "아이디을 입력해주세요.") //@NotBlank는 @NotNull을 포함
    @Size(min = 3,max = 10, message = "아이디는 최소 3자, 최대 10자입니다.")
    @Pattern(regexp = "[a-zA-Z0-9]{3,10}", message = "잘못된 아이디 형식입니다.")
    @ApiModelProperty(example = "test123")
    private String username;

    @NotBlank(message = "비밀 번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 특수문자 포함 최소 8글자입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$", message = "비밀번호는 특수문자 포함 최소 8글자입니다.")
    @ApiModelProperty(example = "abcd1234!")
    private String password;

    @NotBlank(message = "이메일을 입력해주세요.") //@NotBlank는 @NotNull을 포함
    @Size(max = 320, message = "잘못된 이메일 형식입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$", message = "잘못된 이메일 형식입니다.")
    @ApiModelProperty(example = "test123@gmail.com")
    private String email;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "잘못된 휴대폰 번호입니다.")
    @ApiModelProperty(example = "010-1234-5678")
    private String phoneNumber;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 8, message = "닉네임은 한글 최소 2자, 최대 8자까지 입니다.")
    @Pattern(regexp = "[가-힣]{2,8}", message = "닉네임은 한글 최소 2자, 최대 8자까지 입니다.")
    @ApiModelProperty(example = "소고기")
    private String nickName;

    @NotNull(message = "나이를 입력해주세요.")
    @ApiModelProperty(example = "23")
    private Integer age;

    @NotNull(message = "성별을 입력해주세요.")
    @ApiModelProperty(example = "M")
    private Character gender;

}
