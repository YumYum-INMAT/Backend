package yumyum.demo.src.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NickNameDto {
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 8, message = "닉네임은 한글 최소 2자, 최대 8자까지 입니다.")
    @Pattern(regexp = "[가-힣]{2,8}", message = "닉네임은 한글 최소 2자, 최대 8자까지 입니다.")
    private String nickName;
}
