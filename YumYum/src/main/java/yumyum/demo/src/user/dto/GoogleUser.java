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
    public String id;
    public String email;
    public Boolean verifiedEmail;
    public String name;
    public String givenName;
    public String familyName;
    public String picture;
    public String locale;

    public UserEntity toEntity(String password){
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        return UserEntity.builder()
                .email(this.email)
                .password(password)
                .authorities(Collections.singleton(authority))
                .logInType(LogInType.GOOGLE)
                .age(20) //일단 넣어두기, 나중에 빼야함!!
                .gender('F') //일단 넣어두기, 나중에 빼야함!!
                .nickName("구글유저") //일단 넣어두기, 나중에 빼야함!!
                .username(this.email.substring(0, this.email.indexOf("@")))
                .snsId(this.id)
                .build();
    }
}
