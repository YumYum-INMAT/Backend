package yumyum.demo.src.user.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yumyum.demo.config.LogInType;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserProfileDto {
    private Long userId;

    private String email;

    private String profileImgUrl;

    private String nickName;

    private Integer age;

    private Character gender;

    private LogInType logInType;
}
