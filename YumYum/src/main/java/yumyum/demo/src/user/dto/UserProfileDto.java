package yumyum.demo.src.user.dto;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {

    @Size(min = 1, message = "null 값 또는 URL을 입력해주세요") // NULL값 허용, But "", " " 등 빈칸 허용X
    @URL(message = "올바른 URL이 아닙니다.")
    private String profileImgUrl;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 8, message = "닉네임은 한글 최소 2자, 최대 8자까지 입니다.")
    @Pattern(regexp = "[가-힣]{2,8}", message = "닉네임은 한글 최소 2자, 최대 8자까지 입니다.")
    private String nickName;

    @NotNull(message = "나이를 입력해주세요.")
    private Integer age;

    @NotNull(message = "성별을 입력해주세요.")
    private Character gender;
}
