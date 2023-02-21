package yumyum.demo.src.user.dto;

import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import yumyum.demo.config.LogInType;
import yumyum.demo.src.user.entity.Authority;
import yumyum.demo.src.user.entity.UserEntity;

@AllArgsConstructor
@Getter
@Setter
public class GoogleUser {
    private String id;
    private String email;
    private Boolean verifiedEmail;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
    private String locale;

    public UserEntity toEntity(String password){
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        return UserEntity.builder()
                .email("NONE")
                .password(password)
                .authorities(Collections.singleton(authority))
                .logInType(LogInType.GOOGLE)
                .age(20) //일단 넣어두기, 나중에 빼야함!!
                .gender('F') //일단 넣어두기, 나중에 빼야함!!
                .nickName("구글유저") //일단 넣어두기, 나중에 빼야함!!
                .snsId(this.id)
                .build();
    }
}
