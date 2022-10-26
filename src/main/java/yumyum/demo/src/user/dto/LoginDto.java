package yumyum.demo.src.user.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotNull
    @Size(min = 3, max = 20)
    private String email;

    @NotNull
    @Size(min = 3, max = 20)
    private String password;
}
