package yumyum.demo.src.user.dto;

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
public class UserProfileDto {

    private String profileImgUrl;

    private String nickName;

    private Integer age;

    private Character gender;
}
