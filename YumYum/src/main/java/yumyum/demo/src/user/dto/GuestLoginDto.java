package yumyum.demo.src.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GuestLoginDto {
    private Long userId;
    private String email;
    private String password;
}
