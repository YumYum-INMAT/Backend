package yumyum.demo.src.user.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SocialLoginDto {
    @NotBlank(message = "엑세스 토큰 값을 입력해주세요.")
    private String accessToken;
}
